package com.example.bankcard_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCreateCardRequest {
    @NotNull
    private Long userId;

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")

    private String cardNumber;

    private String expiry; // MM/yyyy
}
