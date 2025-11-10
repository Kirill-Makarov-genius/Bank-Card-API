package com.example.bankcard_api.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bankcard_api.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    

    private final UserRepository userRepository;

    UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
