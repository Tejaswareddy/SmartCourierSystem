package com.capg.smartcourier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.capg.smartcourier.entity.User;
import com.capg.smartcourier.exception.ResourceNotFoundException;
import com.capg.smartcourier.repository.AuthRepository;
import com.capg.smartcourier.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock // mock annotation is used for specifying that we are mocking this files
    private AuthRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService service;



    // ✅ TEST 1: Register User
    @Test
    void testRegisterSuccess() {

        User user = new User();
        user.setUsername("sameer");
        user.setPassword("1234");

        when(encoder.encode("1234")).thenReturn("encodedPass");
        when(repo.save(user)).thenReturn(user); // ✅ important

        String result = service.register(user);

        assertEquals("User registered", result); // ✅ fixed
        verify(repo, times(1)).save(user);
    }
    // ✅ TEST 2: Login Success
    @Test
    void testLoginSuccess() {

        User input = new User();
        input.setUsername("sameer");
        input.setPassword("1234");

        User dbUser = new User();
        dbUser.setId(1L);
        dbUser.setUsername("sameer");
        dbUser.setPassword("encodedPass");

        when(repo.findByUsername("sameer")).thenReturn(Optional.of(dbUser));
        when(encoder.matches("1234", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("sameer", 1L)).thenReturn("token123");

        String result = service.login(input);

        assertEquals("token123", result);
    }

    // ❌ TEST 3: User Not Found
    @Test
    void testLoginUserNotFound() {

        when(repo.findByUsername("sameer")).thenReturn(Optional.empty());

        User input = new User();
        input.setUsername("sameer");

        assertThrows(ResourceNotFoundException.class, () -> {
            service.login(input);
        });
    }

    // ❌ TEST 4: Invalid Password
    @Test
    void testLoginInvalidPassword() {

        User dbUser = new User();
        dbUser.setUsername("sameer");
        dbUser.setPassword("encodedPass");

        when(repo.findByUsername("sameer")).thenReturn(Optional.of(dbUser));
        when(encoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        User input = new User();
        input.setUsername("sameer");
        input.setPassword("wrongPass");

        assertThrows(IllegalArgumentException.class, () -> {
            service.login(input);
        });
    }
}