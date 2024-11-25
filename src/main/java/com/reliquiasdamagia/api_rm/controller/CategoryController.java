package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Category;
import com.reliquiasdamagia.api_rm.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar categoria: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar categorias: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar categoria: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Categoria excluída com sucesso!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir categoria: " + ex.getMessage());
        }
    }
}
