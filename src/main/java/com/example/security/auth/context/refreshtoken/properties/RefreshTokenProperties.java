package com.example.security.auth.context.refreshtoken.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth.refresh-token")
public class RefreshTokenProperties {
    private final long expirationTime;
    private final long cleanupInterval;
}
