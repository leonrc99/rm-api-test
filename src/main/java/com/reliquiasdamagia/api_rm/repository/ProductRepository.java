package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}
