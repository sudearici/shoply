package com.sudearici.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private BigDecimal amount;

    private String status; // PENDING, SUCCESS, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();
}
