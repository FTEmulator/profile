package com.FTEmulator.profile.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FTEmulator.profile.entity.User;
import com.FTEmulator.profile.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    // Get user
    public User getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    // Register user
    public User createUser(User user) {
        return userRepository.save(user);
    }

}