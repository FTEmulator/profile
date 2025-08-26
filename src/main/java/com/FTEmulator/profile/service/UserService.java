package com.FTEmulator.profile.service;

import java.util.Optional;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.FTEmulator.profile.entity.User;
import com.FTEmulator.profile.grpc.ProfileOuterClass.LoginResponse;
import com.FTEmulator.profile.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private StringEncryptor stringEncryptor;
    
    @Autowired
    private UserRepository userRepository;

    // Get user
    public User getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    // Get User by email
    public User GetByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Register user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Login
    public LoginResponse login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) { User user = userOpt.get();
            // Compare passwords
            if (BCrypt.checkpw(password, user.getPassword())) {
                return LoginResponse.newBuilder()
                    .setUserId(user.getId().toString())
                    .build();
            }
        }
        return LoginResponse.newBuilder() .setUserId("invalid") .build();
    }
}