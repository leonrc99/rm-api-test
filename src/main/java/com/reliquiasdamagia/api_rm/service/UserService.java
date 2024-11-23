package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.repository.ProductRepository;
import com.reliquiasdamagia.api_rm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String getUserEmailById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"))
                .getEmail();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Buscar o usuário autenticado
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Atualizar as informações do perfil do usuário
    public User updateUserProfile(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Atualizando informações não sensíveis, como nome, endereço, cidade, etc.
        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setCity(updatedUser.getCity());
        existingUser.setState(updatedUser.getState());
        existingUser.setCountry(updatedUser.getCountry());

        return userRepository.save(existingUser);  // Salvando as mudanças
    }

    // Adicionar um produto aos favoritos do usuário
    public void addProductToFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        if (!user.getFavorites().contains(product)) {
            user.getFavorites().add(product);  // Adicionando produto aos favoritos
            userRepository.save(user);  // Salvando a atualização
        }
    }

    // Remover um produto dos favoritos do usuário
    public void removeProductFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        user.getFavorites().remove(product);  // Removendo produto dos favoritos
        userRepository.save(user);  // Salvando a atualização
    }

    // Recuperar os favoritos do usuário
    public List<Product> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return user.getFavorites();  // Retornando a lista de favoritos
    }

    // Histórico de compras do usuário (caso você tenha implementado os pedidos)
    public List<Order> getUserOrderHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Supondo que você tenha um relacionamento entre User e Order
        return user.getOrders();  // Retorna os pedidos do usuário
    }
}
