package com.capg.smartcourier.service;

import org.springframework.beans.factory.annotation.Autowired;//used for automatically injecting required dependencies into a class
import org.springframework.security.crypto.password.PasswordEncoder;
//BCryptPasswordEncoder is a class from spring security used for hashing passwords, verifying passwords securely
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capg.smartcourier.entity.User;
import com.capg.smartcourier.exception.ResourceNotFoundException;
import com.capg.smartcourier.repository.AuthRepository;
import com.capg.smartcourier.security.JwtUtil;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(User user) {
        logger.info("Registering new user: {}", user.getUsername());
        try {
            user.setPassword(encoder.encode(user.getPassword())); //This is where the password gets encoded into hash format
            repo.save(user); //save is method in repository that helps us to send the data into databasej
            logger.info("User {} registered successfully", user.getUsername());
            return "User registered";
        } catch (Exception e) {
            logger.error("Error registering user {}: {}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public String login(User user) {
        logger.debug("Login attempt for user: {}", user.getUsername());
        try {
            User dbUser = repo.findByUsername(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), dbUser.getId());
                logger.info("User {} logged in successfully", user.getUsername());
                return token;
            } else {
                logger.warn("Invalid credentials for user: {}", user.getUsername());
                throw new IllegalArgumentException("Invalid credentials");
            }
        } catch (ResourceNotFoundException e) {
            logger.warn("Login failed: User not found - {}", user.getUsername());
            throw e;
        } catch (Exception e) {
            logger.error("Login error for user {}: {}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
}