package com.example.bankcard_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.bankcard_api.enums.CardStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminCardDTO {
    private Long id;
    private Long userId;
    private String username;
    private String cardNumber; // raw
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private String expiry;
    private CardStatus status;
}
