package com.furniture.ecommerce.repository;

import com.furniture.ecommerce.model.GuestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestOrderRepository extends JpaRepository<GuestOrder, Long> {
    
    Optional<GuestOrder> findByOrderNumber(String orderNumber);
    
    List<GuestOrder> findByEmailOrderByOrderDateDesc(String email);
    
    @Query("SELECT go FROM GuestOrder go WHERE go.email = :email AND go.status != 'CANCELLED' ORDER BY go.orderDate DESC")
    List<GuestOrder> findActiveOrdersByEmail(@Param("email") String email);
    
    boolean existsByOrderNumber(String orderNumber);
}