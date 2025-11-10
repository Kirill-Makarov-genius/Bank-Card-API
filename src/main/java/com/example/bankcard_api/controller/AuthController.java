package com.example.bankcard_api.controller;



import com.example.bankcard_api.DTO.AuthRequest;
import com.example.bankcard_api.DTO.AuthResponse;
import com.example.bankcard_api.DTO.RegisterRequest;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.Role;
import com.example.bankcard_api.repository.UserRepository;
import com.example.bankcard_api.utils.JwtUtill;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtill jwtUtill;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        System.out.println(request);
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtill.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
