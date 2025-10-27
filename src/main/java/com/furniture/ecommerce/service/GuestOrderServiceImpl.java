package com.furniture.ecommerce.service;

import com.furniture.ecommerce.dto.AddressRequestDTO;
import com.furniture.ecommerce.dto.GuestOrderRequestDTO;
import com.furniture.ecommerce.dto.GuestOrderResponseDTO;
import com.furniture.ecommerce.dto.OrderItemRequestDTO;
import com.furniture.ecommerce.model.*;
import com.furniture.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GuestOrderServiceImpl implements GuestOrderService {
    
    private final GuestOrderRepository guestOrderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final CouponService couponService;
    
    @Override
    public GuestOrderResponseDTO createGuestOrder(GuestOrderRequestDTO request) {
        log.info("Creating guest order for email: {}", request.getEmail());
        
        // Create the guest order entity
        GuestOrder guestOrder = new GuestOrder();
        guestOrder.setEmail(request.getEmail());
        guestOrder.setFirstName(request.getFirstName());
        guestOrder.setLastName(request.getLastName());
        guestOrder.setPhone(request.getPhone());
        guestOrder.setTaxAmount(request.getTaxAmount());
        guestOrder.setShippingCost(request.getShippingCost());
        
        // Set addresses
        guestOrder.setShippingAddress(mapToAddressInfo(request.getShippingAddress()));
        guestOrder.setBillingAddress(mapToAddressInfo(request.getBillingAddress()));
        
        // Set estimated delivery date (e.g., 7 days from now)
        guestOrder.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        
        // Save the order first to get an ID
        guestOrder = guestOrderRepository.save(guestOrder);
        
        // Add order items
        for (OrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemRequest.getProductId()));
            
            // Check stock availability
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setGuestOrder(guestOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            guestOrder.addItem(orderItem);
            
            // Update product stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
        }
        
        // Apply coupon if provided
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            applyCouponToOrderInternal(guestOrder, request.getCouponCode());
        }
        
        // Save the final order
        guestOrder = guestOrderRepository.save(guestOrder);
        
        // Create initial status history
        createStatusHistory(guestOrder, null, OrderStatus.PENDING, "Order created");
        
        log.info("Successfully created guest order with ID: {} and order number: {}", 
                guestOrder.getId(), guestOrder.getOrderNumber());
        
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public GuestOrderResponseDTO getGuestOrderByOrderNumber(String orderNumber) {
        log.info("Retrieving guest order by order number: {}", orderNumber);
        
        GuestOrder guestOrder = guestOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
        
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public GuestOrderResponseDTO getGuestOrderById(Long id) {
        log.info("Retrieving guest order by ID: {}", id);
        
        GuestOrder guestOrder = guestOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GuestOrderResponseDTO> getGuestOrdersByEmail(String email) {
        log.info("Retrieving guest orders for email: {}", email);
        
        List<GuestOrder> guestOrders = guestOrderRepository.findByEmailOrderByOrderDateDesc(email);
        
        return guestOrders.stream()
                .map(GuestOrderResponseDTO::fromGuestOrder)
                .toList();
    }
    
    @Override
    public GuestOrderResponseDTO updateOrderStatus(Long orderId, String status, String notes) {
        log.info("Updating order status for order ID: {} to status: {}", orderId, status);
        
        GuestOrder guestOrder = guestOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        OrderStatus previousStatus = guestOrder.getStatus();
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        
        guestOrder.setStatus(newStatus);
        guestOrder = guestOrderRepository.save(guestOrder);
        
        // Create status history entry
        createStatusHistory(guestOrder, previousStatus, newStatus, notes);
        
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    public void cancelGuestOrder(Long orderId, String reason) {
        log.info("Cancelling guest order with ID: {}, reason: {}", orderId, reason);
        
        GuestOrder guestOrder = guestOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Restore product stock
        for (OrderItem item : guestOrder.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        OrderStatus previousStatus = guestOrder.getStatus();
        guestOrder.setStatus(OrderStatus.CANCELLED);
        guestOrderRepository.save(guestOrder);
        
        // Create status history entry
        createStatusHistory(guestOrder, previousStatus, OrderStatus.CANCELLED, reason);
        
        log.info("Successfully cancelled guest order with ID: {}", orderId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GuestOrderResponseDTO> getAllGuestOrders() {
        log.info("Retrieving all guest orders");
        
        List<GuestOrder> guestOrders = guestOrderRepository.findAll();
        
        return guestOrders.stream()
                .map(GuestOrderResponseDTO::fromGuestOrder)
                .toList();
    }
    
    @Override
    public GuestOrderResponseDTO applyCouponToOrder(Long orderId, String couponCode) {
        log.info("Applying coupon {} to order ID: {}", couponCode, orderId);
        
        GuestOrder guestOrder = guestOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Validate order can have coupon applied (e.g., not shipped, delivered, or cancelled)
        if (guestOrder.getStatus() == OrderStatus.SHIPPED || 
            guestOrder.getStatus() == OrderStatus.DELIVERED || 
            guestOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot apply coupon to order in status: " + guestOrder.getStatus());
        }
        
        applyCouponToOrderInternal(guestOrder, couponCode);
        
        guestOrder = guestOrderRepository.save(guestOrder);
        
        log.info("Successfully applied coupon {} to order ID: {}", couponCode, orderId);
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    public GuestOrderResponseDTO removeCouponFromOrder(Long orderId) {
        log.info("Removing coupon from order ID: {}", orderId);
        
        GuestOrder guestOrder = guestOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Validate order can have coupon removed (e.g., not shipped, delivered, or cancelled)
        if (guestOrder.getStatus() == OrderStatus.SHIPPED || 
            guestOrder.getStatus() == OrderStatus.DELIVERED || 
            guestOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot remove coupon from order in status: " + guestOrder.getStatus());
        }
        
        guestOrder.removeCoupon();
        guestOrder = guestOrderRepository.save(guestOrder);
        
        log.info("Successfully removed coupon from order ID: {}", orderId);
        return GuestOrderResponseDTO.fromGuestOrder(guestOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal validateAndCalculateCouponDiscount(String couponCode, BigDecimal orderAmount) {
        return couponService.calculateDiscount(couponCode, orderAmount);
    }
    
    // Private helper methods
    private void applyCouponToOrderInternal(GuestOrder guestOrder, String couponCode) {
        Optional<Coupon> couponOpt = couponService.validateCoupon(couponCode);
        
        if (couponOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired coupon code: " + couponCode);
        }
        
        Coupon coupon = couponOpt.get();
        
        if (!coupon.canApplyToOrder(guestOrder.getSubtotal())) {
            throw new RuntimeException("Coupon cannot be applied to this order. Minimum order amount: " + 
                                     coupon.getMinimumOrderAmount());
        }
        
        guestOrder.applyCoupon(coupon);
        
        // Apply coupon usage when order is created or confirmed
        if (guestOrder.getId() != null) {
            couponService.applyCouponUsage(couponCode);
        }
    }
    
    private AddressInfo mapToAddressInfo(AddressRequestDTO addressRequest) {
        if (addressRequest == null) {
            return null;
        }
        
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setStreetAddress(addressRequest.getStreetAddress());
        addressInfo.setUnit(addressRequest.getUnit());
        addressInfo.setCity(addressRequest.getCity());
        addressInfo.setState(addressRequest.getState());
        addressInfo.setZipCode(addressRequest.getZipCode());
        addressInfo.setCountry(addressRequest.getCountry());
        
        return addressInfo;
    }
    
    private void createStatusHistory(GuestOrder guestOrder, OrderStatus previousStatus, 
                                   OrderStatus newStatus, String notes) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setGuestOrder(guestOrder);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setNotes(notes);
        
        orderStatusHistoryRepository.save(history);
    }
}