package com.capg.smartcourier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capg.smartcourier.entity.User;
import com.capg.smartcourier.repository.AuthRepository;
import com.capg.smartcourier.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private AuthRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

        return "User Registered";
    }

    public String login(User user) {

        User existing = repo.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(user.getPassword(), existing.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}