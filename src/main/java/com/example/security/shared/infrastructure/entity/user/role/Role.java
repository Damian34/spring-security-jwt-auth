package com.example.security.shared.infrastructure.entity.user.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    private final static String ROLES_SEQUENCE = "roles_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ROLES_SEQUENCE)
    @SequenceGenerator(name = ROLES_SEQUENCE, sequenceName = ROLES_SEQUENCE, allocationSize = 1, initialValue = 1)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
