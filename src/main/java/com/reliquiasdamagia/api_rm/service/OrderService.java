package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.OrderItem;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.entity.enums.OrderStatus;
import com.reliquiasdamagia.api_rm.repository.OrderRepository;
import com.reliquiasdamagia.api_rm.repository.ShoppingCartRepository;
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
}
