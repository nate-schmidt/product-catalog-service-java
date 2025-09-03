package com.furniture.ecommerce.repository;

import com.furniture.ecommerce.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    
    List<OrderStatusHistory> findByGuestOrderIdOrderByChangedAtDesc(Long guestOrderId);
}