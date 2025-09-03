package com.furniture.ecommerce.controller;

import com.furniture.ecommerce.dto.GuestOrderRequestDTO;
import com.furniture.ecommerce.dto.GuestOrderResponseDTO;
import com.furniture.ecommerce.service.GuestOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guest-orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class GuestOrderController {
    
    private final GuestOrderService guestOrderService;
    
    @PostMapping
    public ResponseEntity<GuestOrderResponseDTO> createGuestOrder(@Valid @RequestBody GuestOrderRequestDTO request) {
        log.info("Received request to create guest order for email: {}", request.getEmail());
        
        try {
            GuestOrderResponseDTO response = guestOrderService.createGuestOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating guest order: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{orderNumber}")
    public ResponseEntity<GuestOrderResponseDTO> getGuestOrderByOrderNumber(@PathVariable String orderNumber) {
        log.info("Received request to retrieve guest order with order number: {}", orderNumber);
        
        try {
            GuestOrderResponseDTO response = guestOrderService.getGuestOrderByOrderNumber(orderNumber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving guest order: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<GuestOrderResponseDTO> getGuestOrderById(@PathVariable Long id) {
        log.info("Received request to retrieve guest order with ID: {}", id);
        
        try {
            GuestOrderResponseDTO response = guestOrderService.getGuestOrderById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving guest order: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<GuestOrderResponseDTO>> getGuestOrdersByEmail(@PathVariable String email) {
        log.info("Received request to retrieve guest orders for email: {}", email);
        
        try {
            List<GuestOrderResponseDTO> response = guestOrderService.getGuestOrdersByEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving guest orders by email: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<GuestOrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        log.info("Received request to update order status for order ID: {} to status: {}", id, status);
        
        try {
            GuestOrderResponseDTO response = guestOrderService.updateOrderStatus(id, status, notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelGuestOrder(
            @PathVariable Long id,
            @RequestParam String reason) {
        log.info("Received request to cancel guest order with ID: {}, reason: {}", id, reason);
        
        try {
            guestOrderService.cancelGuestOrder(id, reason);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error cancelling guest order: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<GuestOrderResponseDTO>> getAllGuestOrders() {
        log.info("Received request to retrieve all guest orders");
        
        try {
            List<GuestOrderResponseDTO> response = guestOrderService.getAllGuestOrders();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving all guest orders: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Coupon-related endpoints
    
    @PostMapping("/{id}/coupon")
    public ResponseEntity<GuestOrderResponseDTO> applyCouponToOrder(
            @PathVariable Long id,
            @RequestParam String couponCode) {
        log.info("Received request to apply coupon {} to order ID: {}", couponCode, id);
        
        try {
            GuestOrderResponseDTO response = guestOrderService.applyCouponToOrder(id, couponCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error applying coupon to order: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}/coupon")
    public ResponseEntity<GuestOrderResponseDTO> removeCouponFromOrder(@PathVariable Long id) {
        log.info("Received request to remove coupon from order ID: {}", id);
        
        try {
            GuestOrderResponseDTO response = guestOrderService.removeCouponFromOrder(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing coupon from order: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/coupon/{couponCode}/validate")
    public ResponseEntity<Map<String, Object>> validateCoupon(
            @PathVariable String couponCode,
            @RequestParam BigDecimal orderAmount) {
        log.info("Received request to validate coupon {} for order amount: {}", couponCode, orderAmount);
        
        try {
            BigDecimal discountAmount = guestOrderService.validateAndCalculateCouponDiscount(couponCode, orderAmount);
            
            Map<String, Object> response = Map.of(
                "valid", discountAmount.compareTo(BigDecimal.ZERO) > 0,
                "discountAmount", discountAmount,
                "couponCode", couponCode
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error validating coupon: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "valid", false,
                "discountAmount", BigDecimal.ZERO,
                "couponCode", couponCode,
                "error", e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}