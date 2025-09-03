package com.furniture.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    
    @Column(name = "discount_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountValue; // Either percentage (0-100) or fixed amount
    
    @Column(name = "minimum_order_amount", precision = 12, scale = 2)
    private BigDecimal minimumOrderAmount = BigDecimal.ZERO;
    
    @Column(name = "maximum_discount_amount", precision = 10, scale = 2)
    private BigDecimal maximumDiscountAmount; // Cap for percentage discounts
    
    @Column(name = "usage_limit")
    private Integer usageLimit; // null = unlimited
    
    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;
    
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;
    
    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper methods
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               now.isAfter(validFrom) && 
               now.isBefore(validUntil) &&
               (usageLimit == null || usedCount < usageLimit);
    }
    
    public boolean canApplyToOrder(BigDecimal orderAmount) {
        return isValid() && orderAmount.compareTo(minimumOrderAmount) >= 0;
    }
    
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!canApplyToOrder(orderAmount)) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount;
        if (discountType == DiscountType.PERCENTAGE) {
            discount = orderAmount.multiply(discountValue).divide(new BigDecimal("100"));
            // Apply maximum discount cap if specified
            if (maximumDiscountAmount != null && discount.compareTo(maximumDiscountAmount) > 0) {
                discount = maximumDiscountAmount;
            }
        } else {
            discount = discountValue;
        }
        
        // Ensure discount doesn't exceed order amount
        return discount.min(orderAmount);
    }
    
    public void incrementUsage() {
        this.usedCount++;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum DiscountType {
        PERCENTAGE,
        FIXED_AMOUNT
    }
}