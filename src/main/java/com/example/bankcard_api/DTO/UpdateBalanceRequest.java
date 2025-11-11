package com.example.bankcard_api.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBalanceRequest {

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 13, fraction = 2)
    private BigDecimal balance;
}
