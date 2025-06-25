package com.example.security.auth.context.jwt.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {
    private final String secretKey;
    private final int expirationTime;
}
