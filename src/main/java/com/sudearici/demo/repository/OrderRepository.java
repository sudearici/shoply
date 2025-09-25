package com.sudearici.demo.repository;

import com.sudearici.demo.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
