package com.example.security.auth.context.hashing.service;

import com.example.security.auth.context.hashing.properties.HashingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaltPepperHashService {
    private final HashingProperties hashingProperties;

    public String enrich(String password, char[] salt) {
        String saltStr = String.valueOf(salt);
        return password + saltStr + hashingProperties.getPepper();
    }

    public char[] generateSalt() {
        return UUID.randomUUID().toString().toCharArray();
    }
}
