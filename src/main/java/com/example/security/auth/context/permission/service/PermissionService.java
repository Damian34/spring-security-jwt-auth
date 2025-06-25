package com.example.security.auth.context.permission.service;

import com.example.security.auth.context.permission.properties.PermissionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionProperties permissionProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public List<String> getPermittedPaths() {
        return permissionProperties.getPermitAll();
    }

    public boolean isPermittedPath(String path) {
        return getPermittedPaths().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

}
