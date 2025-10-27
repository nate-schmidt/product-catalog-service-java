package com.furniture.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_order_id", nullable = false)
    private GuestOrder guestOrder;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private OrderStatus previousStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private OrderStatus newStatus;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
    
    @Column(name = "changed_by")
    private String changedBy; // Could be "SYSTEM", "ADMIN", or user identifier
    
    @PrePersist
    protected void onCreate() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
        if (changedBy == null) {
            changedBy = "SYSTEM";
        }
    }
}