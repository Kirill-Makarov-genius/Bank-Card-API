package com.example.bankcard_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can contain letters, digits, and ._-")
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
