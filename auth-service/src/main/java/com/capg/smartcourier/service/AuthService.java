package com.capg.smartcourier.service;

import org.springframework.beans.factory.annotation.Autowired;//used for automatically injecting required dependencies into a class
import org.springframework.security.crypto.password.PasswordEncoder;
//BCryptPasswordEncoder is a class from spring security used for hashing passwords, verifying passwords securely
import org.springframework.stereotype.Service;

import com.capg.smartcourier.entity.User;
import com.capg.smartcourier.exception.ResourceNotFoundException;
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
        user.setPassword(encoder.encode(user.getPassword())); //This is where the password gets encoded into hash format
        repo.save(user); //save is method in repository that helps us to send the data into databasej
        return "User registered";
    }

    public String login(User user) {
    	User dbUser = repo.findByUsername(user.getUsername())
    	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    	if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
    	    return jwtUtil.generateToken(user.getUsername(), dbUser.getId());
    	} else {
    	    throw new IllegalArgumentException("Invalid credentials");
    	}
    }
}