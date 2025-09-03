package com.furniture.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class GuestOrderRequestDTO {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;
    
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequestDTO> items;
    
    @Valid
    private AddressRequestDTO shippingAddress;
    
    @Valid
    private AddressRequestDTO billingAddress;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax amount must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Tax amount must have at most 10 integer digits and 2 decimal places")
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Shipping cost must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Shipping cost must have at most 10 integer digits and 2 decimal places")
    private BigDecimal shippingCost = BigDecimal.ZERO;
    
    // Coupon code field
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]*$", message = "Coupon code must contain only uppercase letters, numbers, underscores, and dashes")
    private String couponCode;
    
    // Constructors
    public GuestOrderRequestDTO() {
    }
    
    public GuestOrderRequestDTO(String email, String firstName, String lastName, String phone, 
                              List<OrderItemRequestDTO> items, AddressRequestDTO shippingAddress, 
                              AddressRequestDTO billingAddress, BigDecimal taxAmount, 
                              BigDecimal shippingCost, String couponCode) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.taxAmount = taxAmount;
        this.shippingCost = shippingCost;
        this.couponCode = couponCode;
    }
    
    // Getters and Setters
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
    
    public List<OrderItemRequestDTO> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }
    
    public AddressRequestDTO getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(AddressRequestDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public AddressRequestDTO getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(AddressRequestDTO billingAddress) {
        this.billingAddress = billingAddress;
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
    
    public String getCouponCode() {
        return couponCode;
    }
    
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}