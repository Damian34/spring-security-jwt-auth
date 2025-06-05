package com.example.security.auth.service.user.validator;

import com.example.security.auth.exception.UserAlreadyExistsException;
import com.example.security.shared.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;

    @Override
    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }
    }
}
