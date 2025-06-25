package com.example.security.auth.context.authorization.service;

import com.example.security.auth.context.authorization.properties.AuthorizationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthorizationProperties authorizationProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public List<String> getPermittedPaths() {
        return authorizationProperties.getPermitAll();
    }

    public boolean isPermittedPath(String path) {
        return getPermittedPaths().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

}
