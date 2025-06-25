package com.example.security.auth.config.properties;

import com.example.security.shared.util.ImmutableUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "auth.cors")
public class CorsProperties {
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private List<String> allowedOrigins;

    @PostConstruct
    public void init() {
        this.allowedHeaders = ImmutableUtils.safeImmutableList(allowedHeaders);
        this.allowedMethods = ImmutableUtils.safeImmutableList(allowedMethods);
        this.allowedOrigins = ImmutableUtils.safeImmutableList(allowedOrigins);
    }
}