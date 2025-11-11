package com.example.bankcard_api.DTO;

import java.time.LocalDateTime;

import com.example.bankcard_api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
}
