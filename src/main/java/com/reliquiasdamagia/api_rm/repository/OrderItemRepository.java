package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
