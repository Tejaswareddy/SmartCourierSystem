package com.capg.smartcourier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.smartcourier.entity.User;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}