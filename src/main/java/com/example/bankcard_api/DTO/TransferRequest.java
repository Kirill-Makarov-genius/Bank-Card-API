package com.example.bankcard_api.DTO;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequest {

    @NotBlank
    @Schema(example = "4111111111111111")
    private String fromCardNumber;

    @NotBlank
    @Schema(example = "5222222222222222")
    private String toCardNumber;

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(example = "100.00")
    private BigDecimal amount;
}
