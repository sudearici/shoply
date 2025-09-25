package com.sudearici.demo.service;

import com.sudearici.demo.domain.Order;
import com.sudearici.demo.domain.Payment;
import com.sudearici.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(Order order) {
        // Dummy payment logic
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("SUCCESS"); // simulate success
        return paymentRepository.save(payment);
    }
}
