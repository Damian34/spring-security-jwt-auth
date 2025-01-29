package com.example.security.shared.infrastructure.repository;

import com.example.security.shared.infrastructure.entity.user.role.Role;
import com.example.security.shared.infrastructure.entity.user.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
