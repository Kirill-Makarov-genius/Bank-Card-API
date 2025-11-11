package com.example.bankcard_api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardCreateRequest {

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    @Schema(example = "4111111111111111")
    private String cardNumber;
}
