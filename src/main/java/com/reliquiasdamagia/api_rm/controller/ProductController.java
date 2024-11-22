package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.ProductDTO;
import com.reliquiasdamagia.api_rm.dto.ProductRequest;
import com.reliquiasdamagia.api_rm.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest request) {
        ProductDTO product = productService.createProduct(request);

        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductByCategoryId(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);

        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        ProductDTO updatedProduct = productService.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Produto excluído com sucesso!");
    }
}
