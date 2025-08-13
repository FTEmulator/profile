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

    public User getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}