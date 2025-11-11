package com.example.bankcard_api.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.exception.UserNotFoundException;
import com.example.bankcard_api.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Page<User> getAllUsersForPage(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User saveUser(User newUser){

        return userRepository.save(newUser);
    }

    public User findUserById(Long id){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return user;    
    }

    public User deleteUserById(Long id){
        return userRepository.deleteUserById(id);
    }

    public User getCurrentUser(Authentication auth){
        String username = auth.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

}
