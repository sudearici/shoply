package com.sudearici.demo.service;

import com.sudearici.demo.domain.Cart;
import com.sudearici.demo.domain.CartItem;
import com.sudearici.demo.domain.Product;
import com.sudearici.demo.domain.User;
import com.sudearici.demo.repository.CartRepository;
import com.sudearici.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Cart addItem(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    cart.getItems().add(ci);
                    return ci;
                });

        item.setQuantity(item.getQuantity() + quantity);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(User user, Long productId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));

        return cartRepository.save(cart);
    }
    @Transactional
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }

    @Transactional
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

}
