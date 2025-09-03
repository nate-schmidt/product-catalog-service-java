package com.furniture.ecommerce.service;

import com.furniture.ecommerce.model.Coupon;
import java.math.BigDecimal;
import java.util.Optional;

public interface CouponService {
    
    /**
     * Validates and retrieves a coupon by its code
     */
    Optional<Coupon> validateCoupon(String couponCode);
    
    /**
     * Validates if a coupon can be applied to an order
     */
    boolean canApplyCoupon(String couponCode, BigDecimal orderAmount);
    
    /**
     * Calculates the discount amount for a given coupon and order amount
     */
    BigDecimal calculateDiscount(String couponCode, BigDecimal orderAmount);
    
    /**
     * Applies a coupon usage (increments usage count)
     */
    void applyCouponUsage(String couponCode);
    
    /**
     * Creates a new coupon
     */
    Coupon createCoupon(Coupon coupon);
    
    /**
     * Finds coupon by code
     */
    Optional<Coupon> findByCode(String code);
}