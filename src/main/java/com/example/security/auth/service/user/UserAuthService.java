package com.example.security.auth.service.user;

import com.example.security.auth.exception.UserNotFoundException;
import com.example.security.shared.model.user.User;
import com.example.security.shared.model.user.UserRepository;
import com.example.security.shared.model.role.Role;
import com.example.security.shared.model.role.RoleName;
import com.example.security.shared.model.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Role getRoleUser() {
        return roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role newRole = new Role(RoleName.ROLE_USER);
                    return roleRepository.save(newRole);
                });
    }
}
