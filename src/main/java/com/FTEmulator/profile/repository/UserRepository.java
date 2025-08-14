package com.FTEmulator.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FTEmulator.profile.entity.User;

// Enables UserRepository to use findAll(), findById(), save(), delete() and more
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByName(String name);
}