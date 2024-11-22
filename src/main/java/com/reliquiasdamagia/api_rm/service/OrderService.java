package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.OrderItem;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.entity.enums.OrderStatus;
import com.reliquiasdamagia.api_rm.repository.OrderRepository;
import com.reliquiasdamagia.api_rm.repository.ProductRepository;
import com.reliquiasdamagia.api_rm.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order createOrder(Long userId) {
        // Obtém o carrinho de compras do usuário
        ShoppingCart cart = shoppingCartRepository.findByUserIdAndStatus(userId, CartStatus.DRAFT)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado."));

        // Valida se o carrinho tem itens
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Carrinho vazio. Não é possível criar um pedido.");
        }

        // Calcula o total do pedido
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cria a entidade Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(totalPrice);

        // Mapeia os itens do carrinho para itens do pedido
        order.setItems(cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).collect(Collectors.toList()));

        // Salva o pedido
        Order savedOrder = orderRepository.save(order);

        // Atualiza o status do carrinho para COMPLETED
        cart.setStatus(CartStatus.COMPLETED);
        shoppingCartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order createOrderFromCart(ShoppingCart cart) {
        // Criar novo pedido
        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setTotalPrice(cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatus.PENDING);

        // Mapear itens do carrinho para itens do pedido
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        // Salvar pedido no banco de dados
        orderRepository.save(order);

        return order;
    }

    public void updateStockAfterOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        order.getItems().forEach(orderItem -> {
            Product product = orderItem.getProduct();

            // Validar se há estoque suficiente
            if (product.getStock() < orderItem.getQuantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + product.getName());
            }

            // Atualizar o estoque
            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepository.save(product);
        });
    }
}
