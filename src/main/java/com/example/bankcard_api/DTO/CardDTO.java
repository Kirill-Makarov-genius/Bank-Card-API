package com.example.bankcard_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CardDTO {
    private Long id;

    private String cardNumber;

    private BigDecimal balance;
    private LocalDateTime createdAt;
}
