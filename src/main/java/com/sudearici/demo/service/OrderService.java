package com.sudearici.demo.service;



import com.sudearici.demo.domain.*;
import com.sudearici.demo.repository.OrderRepository;
import com.sudearici.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(User user) {
        // Eskisi: cartService.cartRepository.findByUser(user)
        Cart cart = cartService.getCartByUser(user); // Yeni public metod kullanıldı

        Order order = new Order();
        order.setUser(user);

        order.setItems(cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + product.getTitle());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            return orderItem;
        }).collect(Collectors.toSet()));

        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        // Cart temizle
        cart.getItems().clear();
        cartService.saveCart(cart); // Yeni public metod ile kaydet

        return orderRepository.save(order);
    }
}