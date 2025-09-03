package com.furniture.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guest_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber; // Generated: ORD-YYYY-XXXXXXXX
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    private String phone;
    
    @OneToMany(mappedBy = "guestOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    // Coupon information
    @Column(name = "coupon_code")
    private String couponCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon appliedCoupon;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    // Shipping Address (embedded)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "streetAddress", column = @Column(name = "shipping_street")),
        @AttributeOverride(name = "unit", column = @Column(name = "shipping_unit")),
        @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip")),
        @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
    })
    private AddressInfo shippingAddress;
    
    // Billing Address (embedded)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "streetAddress", column = @Column(name = "billing_street")),
        @AttributeOverride(name = "unit", column = @Column(name = "billing_unit")),
        @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
        @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip")),
        @AttributeOverride(name = "country", column = @Column(name = "billing_country"))
    })
    private AddressInfo billingAddress;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;
    
    // Helper methods
    public void addItem(OrderItem item) {
        items.add(item);
        item.setGuestOrder(this);
        recalculateTotals();
    }
    
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setGuestOrder(null);
        recalculateTotals();
    }
    
    public void applyCoupon(Coupon coupon) {
        if (coupon != null && coupon.canApplyToOrder(subtotal)) {
            this.appliedCoupon = coupon;
            this.couponCode = coupon.getCode();
            this.discountAmount = coupon.calculateDiscount(subtotal);
            recalculateTotals();
        }
    }
    
    public void removeCoupon() {
        this.appliedCoupon = null;
        this.couponCode = null;
        this.discountAmount = BigDecimal.ZERO;
        recalculateTotals();
    }
    
    private void recalculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate total: subtotal + tax + shipping - discount
        this.totalAmount = subtotal
                .add(taxAmount)
                .add(shippingCost)
                .subtract(discountAmount);
    }
    
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        if (orderNumber == null) {
            orderNumber = generateOrderNumber();
        }
        recalculateTotals();
    }
    
    @PreUpdate
    protected void onUpdate() {
        recalculateTotals();
    }
    
    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().getYear() + "-" + 
               String.format("%08d", System.currentTimeMillis() % 100000000);
    }
}