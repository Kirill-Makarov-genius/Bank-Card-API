package com.example.bankcard_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.bankcard_api.enums.CardStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CardDTO {
    
    @Hidden
    private Long id;

    @JsonIgnore
    private String cardNumber;

    private BigDecimal balance;
    
    @Hidden
    private LocalDateTime createdAt;

    
    private String expiry;
    private CardStatus status;

    @JsonProperty("cardNumber")
    @Schema(description = "Masked card number", example = "**** **** **** 1234")
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }
}
