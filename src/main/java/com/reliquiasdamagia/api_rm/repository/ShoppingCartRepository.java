package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserIdAndStatus(Long userId, CartStatus status);
}
