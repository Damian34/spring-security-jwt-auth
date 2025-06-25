package com.example.security.auth.context.hashing.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security.hashing")
public class HashingProperties {
    private final String pepper;
}
