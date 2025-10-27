package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.GuestOrderRequestDTO;
import com.furniture.ecommerce.dto.GuestOrderResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface GuestOrderService {
    
    GuestOrderResponseDTO createGuestOrder(GuestOrderRequestDTO request);
    
    GuestOrderResponseDTO getGuestOrderByOrderNumber(String orderNumber);
    
    GuestOrderResponseDTO getGuestOrderById(Long id);
    
    List<GuestOrderResponseDTO> getGuestOrdersByEmail(String email);
    
    GuestOrderResponseDTO updateOrderStatus(Long orderId, String status, String notes);
    
    void cancelGuestOrder(Long orderId, String reason);
    
    List<GuestOrderResponseDTO> getAllGuestOrders();
    
    // Coupon-related methods
    GuestOrderResponseDTO applyCouponToOrder(Long orderId, String couponCode);
    
    GuestOrderResponseDTO removeCouponFromOrder(Long orderId);
    
    BigDecimal validateAndCalculateCouponDiscount(String couponCode, BigDecimal orderAmount);
}