package com.FTEmulator.profile.service;

import java.util.Optional;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Register user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Login
    public LoginResponse login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Decrypt
            String decryptedPassword = stringEncryptor.decrypt(user.getPassword());

            // Compare passwords
            if (decryptedPassword.equals(password)) {
                return LoginResponse.newBuilder()
                    .setUserId(user.getId().toString())
                    .build();
            }
        }

        return null;
    }

}