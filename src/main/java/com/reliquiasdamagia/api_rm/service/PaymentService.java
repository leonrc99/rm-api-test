package com.reliquiasdamagia.api_rm.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;

    public String createPaymentFromOrder(Order order, String payerEmail) throws MPException, MPApiException {
        // Configuração do cliente de preferência
        PreferenceClient preferenceClient = new PreferenceClient();

        // Criar itens da preferência com base nos itens da ordem
        List<PreferenceItemRequest> items = order.getItems().stream()
                .map(orderItem -> PreferenceItemRequest.builder()
                        .title(orderItem.getProduct().getName())
                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        // Criar a solicitação de preferência
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(PreferencePayerRequest.builder().email(payerEmail).build())
                .build();

        // Criar preferência no Mercado Pago
        Preference preference = preferenceClient.create(preferenceRequest);

        // Atualizar a ordem com informações da preferência
        order.setPaymentId(preference.getId());
        order.setPaymentStatus("INITIATED");
        orderRepository.save(order);

        // Retorna o link de redirecionamento
        return preference.getInitPoint();
    }

    @Transactional
    public boolean processPaymentNotification(Long paymentId) {
        try {
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId); // Buscar detalhes do pagamento

            if ("approved".equalsIgnoreCase(payment.getStatus())) {
                // Buscar carrinho associado (usando externalReference como ID do carrinho)
                Long userId = Long.parseLong(payment.getExternalReference());
                ShoppingCart cart = shoppingCartService.getCart(userId);

                // Transformar carrinho em pedido
                Order order = orderService.createOrderFromCart(cart);

                // Atualizar estoque
                orderService.updateStockAfterOrder(order.getId());

                // Marcar carrinho como COMPLETED
                shoppingCartService.completeCart(cart);

                return true;
            }
            return false;
        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            return false;
        }
    }
}
