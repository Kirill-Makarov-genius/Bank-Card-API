package com.example.bankcard_api.seeder;

import com.example.bankcard_api.entity.Card;
import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.CardStatus;
import com.example.bankcard_api.enums.Role;
import com.example.bankcard_api.repository.CardRepository;
import com.example.bankcard_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() > 0) {
            
            return;
        }

        for (int i = 1; i <= 50; i++) {
            
            User user = User.builder()
                    .username("user" + i)
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now().minusDays(50 - i))
                    .build();

            userRepository.save(user);

            
            Card card = Card.builder()
                    .user(user)
                    .cardNumber(String.format("%016d", 1111000000000000L + i))
                    .balance(BigDecimal.valueOf(1000 + i * 50))
                    .createdAt(user.getCreatedAt())
                    .expiry("12/2028")
                    .status(CardStatus.ACTIVE)
                    .build();

            cardRepository.save(card);
        }

    
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
        }

        System.out.println("Database seeding complete: 50 users + cards + admin");
    }
}
