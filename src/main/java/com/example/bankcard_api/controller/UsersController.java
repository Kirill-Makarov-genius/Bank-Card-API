package com.example.bankcard_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.Role;
import com.example.bankcard_api.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/users")
public class UsersController {
    

    private final UserService userService;

    public UsersController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping("/user/register")
    public User createUser() {
        
        return userService.saveUser(null);

    }

    
    
}
