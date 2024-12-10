package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.ProductDTO;
import com.reliquiasdamagia.api_rm.dto.ProductRequest;
import com.reliquiasdamagia.api_rm.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            ProductDTO product = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar produto: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar produtos: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar produto: " + ex.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long categoryId) {
        try {
            List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar produtos: " + ex.getMessage());
        }
    }

    @GetMapping("/category-name/{categoryName}")
    public ResponseEntity<?> getProductsByCategoryName(@PathVariable String categoryName) {
        try {
            List<ProductDTO> products = productService.getProductsByCategoryName(categoryName);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar produtos: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar produto: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir produto: " + ex.getMessage());
        }
    }
}
