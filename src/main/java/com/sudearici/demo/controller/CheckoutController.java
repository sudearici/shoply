package com.sudearici.demo.controller;

import com.sudearici.demo.domain.Order;
import com.sudearici.demo.domain.Payment;
import com.sudearici.demo.domain.User;
import com.sudearici.demo.service.OrderService;
import com.sudearici.demo.service.PaymentService;
import com.sudearici.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService; // JWT token ile kullanıcı alacağız

    @PostMapping
    public ResponseEntity<String> checkout(@RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromToken(authHeader);
        Order order = orderService.createOrder(user);
        Payment payment = paymentService.processPayment(order);

        if ("SUCCESS".equals(payment.getStatus())) {
            return ResponseEntity.ok("Payment successful. Order ID: " + order.getId());
        } else {
            return ResponseEntity.status(400).body("Payment failed");
        }
    }
}
