package com.example.bankcard_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.Role;
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

    public User saveUser(User newUser){

        return userRepository.save(newUser);
    }

}
