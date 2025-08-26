package com.FTEmulator.profile.security;

import java.security.SecureRandom;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class jasyptConf {

    @Bean
    public StringEncryptor stringEncryptor() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String randomPassword = java.util.Base64.getEncoder().encodeToString(keyBytes);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(randomPassword);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor;
    }
}