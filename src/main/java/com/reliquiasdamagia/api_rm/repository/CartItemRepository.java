package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
