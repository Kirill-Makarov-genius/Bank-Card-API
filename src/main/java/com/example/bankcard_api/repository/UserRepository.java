package com.example.bankcard_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankcard_api.entity.User;
import java.util.Optional;




@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    
    Page<User> findAll(Pageable pageable);

    User deleteUserById(Long id);

    boolean existsByUsername(String username);
}