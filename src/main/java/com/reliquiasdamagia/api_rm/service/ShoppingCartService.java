package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.CartItem;
import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.repository.ProductRepository;
import com.reliquiasdamagia.api_rm.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.DRAFT)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();

                    if (cart.getItems() == null) {
                        cart.setItems(new ArrayList<>());
                    }

                    cart.setUserId(userId);
                    cart.setStatus(CartStatus.DRAFT);
                    return cartRepository.save(cart);
                });
    }

    public void addItemToCart(Long userId, Long productId, int quantity) {
        ShoppingCart cart = getOrCreateCart(userId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            // Usando BigDecimal para a multiplicação
            item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            if (product.getStock() < quantity) {
                throw new RuntimeException("Estoque insuficiente");
            }

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            // Usando BigDecimal para a multiplicação
            item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(item);
        }

        cartRepository.save(cart);
    }

    public void removeItemFromCart(Long userId, Long itemId) {
        ShoppingCart cart = getOrCreateCart(userId);

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    public ShoppingCart getCart(Long userId) {
        return getOrCreateCart(userId);
    }

    public void clearCart(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public void completeCart(ShoppingCart cart) {
        cart.setStatus(CartStatus.COMPLETED);
        cartRepository.save(cart);
    }
}
