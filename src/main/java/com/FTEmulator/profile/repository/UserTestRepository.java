package com.FTEmulator.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FTEmulator.profile.entity.User;

public interface UserTestRepository extends JpaRepository<User, UUID> {
    User findByName(String name);
}