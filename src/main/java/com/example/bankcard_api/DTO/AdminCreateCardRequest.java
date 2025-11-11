package com.example.bankcard_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import com.example.bankcard_api.validation.ValidCardNumber;
import com.example.bankcard_api.validation.ValidExpiry;

@Data
public class AdminCreateCardRequest {
    @NotNull
    private Long userId;

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    @ValidCardNumber
    private String cardNumber;

    // Optional; if null, service will compute expiry dynamically
    @ValidExpiry
    private String expiry; // MM/yyyy
}
