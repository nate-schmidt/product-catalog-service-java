package com.furniture.ecommerce.repository;

import com.furniture.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByGuestOrderId(Long guestOrderId);
    
    void deleteByGuestOrderId(Long guestOrderId);
}