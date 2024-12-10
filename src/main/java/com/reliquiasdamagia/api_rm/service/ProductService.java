package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.dto.ProductDTO;
import com.reliquiasdamagia.api_rm.dto.ProductRequest;
import com.reliquiasdamagia.api_rm.entity.Category;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.repository.CategoryRepository;
import com.reliquiasdamagia.api_rm.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImages(request.getImages());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return toProductDTO(savedProduct);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return toProductDTO(product);
    }

    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        List<Product> products = productRepository.findByCategoryName(categoryName);

        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImages(request.getImages());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return toProductDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImages(product.getImages());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }
}
