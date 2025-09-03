package com.furniture.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GuestOrderResponseDTO {
    
    private Long id;
    private String orderNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private List<OrderItemResponseDTO> items;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal totalAmount;
    private AddressResponseDTO shippingAddress;
    private AddressResponseDTO billingAddress;
    private LocalDateTime orderDate;
    private LocalDate estimatedDeliveryDate;
    
    // Coupon information
    private String couponCode;
    private String couponDescription;
    private BigDecimal discountAmount;
    private String discountType;
    
    // Constructors
    public GuestOrderResponseDTO() {
    }
    
    public GuestOrderResponseDTO(Long id, String orderNumber, String email, String firstName, 
                               String lastName, String phone, List<OrderItemResponseDTO> items, 
                               String status, BigDecimal subtotal, BigDecimal taxAmount, 
                               BigDecimal shippingCost, BigDecimal totalAmount, 
                               AddressResponseDTO shippingAddress, AddressResponseDTO billingAddress, 
                               LocalDateTime orderDate, LocalDate estimatedDeliveryDate,
                               String couponCode, String couponDescription, BigDecimal discountAmount,
                               String discountType) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.items = items;
        this.status = status;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.shippingCost = shippingCost;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderDate = orderDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.couponCode = couponCode;
        this.couponDescription = couponDescription;
        this.discountAmount = discountAmount;
        this.discountType = discountType;
    }
    
    // Factory method for creating from GuestOrder entity
    public static GuestOrderResponseDTO fromGuestOrder(com.furniture.ecommerce.model.GuestOrder guestOrder) {
        GuestOrderResponseDTO dto = new GuestOrderResponseDTO();
        dto.setId(guestOrder.getId());
        dto.setOrderNumber(guestOrder.getOrderNumber());
        dto.setEmail(guestOrder.getEmail());
        dto.setFirstName(guestOrder.getFirstName());
        dto.setLastName(guestOrder.getLastName());
        dto.setPhone(guestOrder.getPhone());
        
        if (guestOrder.getItems() != null) {
            dto.setItems(guestOrder.getItems().stream()
                    .map(OrderItemResponseDTO::fromOrderItem)
                    .toList());
        }
        
        dto.setStatus(guestOrder.getStatus() != null ? guestOrder.getStatus().toString() : null);
        dto.setSubtotal(guestOrder.getSubtotal());
        dto.setTaxAmount(guestOrder.getTaxAmount());
        dto.setShippingCost(guestOrder.getShippingCost());
        dto.setTotalAmount(guestOrder.getTotalAmount());
        
        if (guestOrder.getShippingAddress() != null) {
            dto.setShippingAddress(AddressResponseDTO.fromAddressInfo(guestOrder.getShippingAddress()));
        }
        
        if (guestOrder.getBillingAddress() != null) {
            dto.setBillingAddress(AddressResponseDTO.fromAddressInfo(guestOrder.getBillingAddress()));
        }
        
        dto.setOrderDate(guestOrder.getOrderDate());
        dto.setEstimatedDeliveryDate(guestOrder.getEstimatedDeliveryDate());
        
        // Coupon information
        dto.setCouponCode(guestOrder.getCouponCode());
        dto.setDiscountAmount(guestOrder.getDiscountAmount());
        
        if (guestOrder.getAppliedCoupon() != null) {
            dto.setCouponDescription(guestOrder.getAppliedCoupon().getDescription());
            dto.setDiscountType(guestOrder.getAppliedCoupon().getDiscountType().toString());
        }
        
        return dto;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<OrderItemResponseDTO> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getShippingCost() {
        return shippingCost;
    }
    
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public AddressResponseDTO getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(AddressResponseDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public AddressResponseDTO getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(AddressResponseDTO billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }
    
    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
    
    public String getCouponCode() {
        return couponCode;
    }
    
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    
    public String getCouponDescription() {
        return couponDescription;
    }
    
    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}