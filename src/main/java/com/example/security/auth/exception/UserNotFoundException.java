package com.example.security.auth.exception;

import com.example.security.shared.exception.GlobalException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@AllArgsConstructor
public class UserNotFoundException extends GlobalException {
    private String username;

    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("User with username \"{0}\" not found", username);
    }
}
