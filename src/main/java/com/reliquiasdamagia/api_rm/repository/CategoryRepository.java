package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
