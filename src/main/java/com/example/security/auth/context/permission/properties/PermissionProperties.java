package com.example.security.auth.context.permission.properties;

import com.example.security.shared.util.ImmutableUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "auth.permission")
public class PermissionProperties {
    private List<String> permitAll;

    @PostConstruct
    public void init() {
        this.permitAll = ImmutableUtils.safeImmutableList(permitAll);
    }
}
