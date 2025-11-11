package com.example.bankcard_api.service;

import com.example.bankcard_api.entity.User;
import com.example.bankcard_api.enums.Role;
import com.example.bankcard_api.exception.UserNotFoundException;
import com.example.bankcard_api.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersForPage() {
        User user1 = new User();
        User user2 = new User();
        Page<User> page = new PageImpl<>(List.of(user1, user2));
        when(userRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        Page<User> result = userService.getAllUsersForPage(PageRequest.of(0, 2));

        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(PageRequest.of(0, 2));
    }

    @Test
    void testSaveUser() {
        User newUser = User.builder().username("user1").role(Role.USER).build();
        when(userRepository.save(newUser)).thenReturn(newUser);

        User saved = userService.saveUser(newUser);

        assertEquals("user1", saved.getUsername());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testFindUserByIdFound() {
        User user = User.builder().id(1L).username("user1").role(Role.USER).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.findUserById(1L);

        assertEquals(1L, found.getId());
        assertEquals("user1", found.getUsername());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    void testGetCurrentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user1");

        User user = User.builder().username("user1").role(Role.USER).build();
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        User current = userService.getCurrentUser(auth);

        assertEquals("user1", current.getUsername());
        verify(userRepository, times(1)).findByUsername("user1");
    }

    @Test
    void testGetCurrentUserNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getCurrentUser(auth));
    }

    @Test
    void testDeleteUserById() {
        User user = User.builder().id(1L).username("user1").build();
        when(userRepository.deleteUserById(1L)).thenReturn(user);

        User deleted = userService.deleteUserById(1L);

        assertEquals(1L, deleted.getId());
        verify(userRepository, times(1)).deleteUserById(1L);
    }
}
