package com.furniture.ecommerce.service;

import com.furniture.ecommerce.model.Coupon;
import com.furniture.ecommerce.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponServiceImpl implements CouponService {
    
    private final CouponRepository couponRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Coupon> validateCoupon(String couponCode) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return Optional.empty();
        }
        
        log.debug("Validating coupon code: {}", couponCode);
        return couponRepository.findValidCouponByCode(couponCode.toUpperCase().trim(), LocalDateTime.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canApplyCoupon(String couponCode, BigDecimal orderAmount) {
        Optional<Coupon> couponOpt = validateCoupon(couponCode);
        
        if (couponOpt.isEmpty()) {
            log.debug("Coupon {} is not valid", couponCode);
            return false;
        }
        
        Coupon coupon = couponOpt.get();
        boolean canApply = coupon.canApplyToOrder(orderAmount);
        
        log.debug("Coupon {} can be applied to order amount {}: {}", 
                 couponCode, orderAmount, canApply);
        
        return canApply;
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateDiscount(String couponCode, BigDecimal orderAmount) {
        Optional<Coupon> couponOpt = validateCoupon(couponCode);
        
        if (couponOpt.isEmpty()) {
            log.debug("Cannot calculate discount - coupon {} is not valid", couponCode);
            return BigDecimal.ZERO;
        }
        
        Coupon coupon = couponOpt.get();
        BigDecimal discount = coupon.calculateDiscount(orderAmount);
        
        log.debug("Calculated discount for coupon {} on order amount {}: {}", 
                 couponCode, orderAmount, discount);
        
        return discount;
    }
    
    @Override
    public void applyCouponUsage(String couponCode) {
        Optional<Coupon> couponOpt = couponRepository.findByCode(couponCode.toUpperCase().trim());
        
        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            coupon.incrementUsage();
            couponRepository.save(coupon);
            
            log.info("Applied usage for coupon {}: new usage count {}", 
                    couponCode, coupon.getUsedCount());
        } else {
            log.warn("Attempted to apply usage for non-existent coupon: {}", couponCode);
        }
    }
    
    @Override
    public Coupon createCoupon(Coupon coupon) {
        // Ensure code is uppercase and trimmed
        if (coupon.getCode() != null) {
            coupon.setCode(coupon.getCode().toUpperCase().trim());
        }
        
        log.info("Creating new coupon with code: {}", coupon.getCode());
        return couponRepository.save(coupon);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Coupon> findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return couponRepository.findByCode(code.toUpperCase().trim());
    }
}