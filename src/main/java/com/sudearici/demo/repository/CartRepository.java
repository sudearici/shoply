package com.sudearici.demo.repository;

import com.sudearici.demo.domain.Cart;
import com.sudearici.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
