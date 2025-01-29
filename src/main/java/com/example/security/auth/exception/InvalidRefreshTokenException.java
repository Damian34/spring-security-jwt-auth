package com.example.security.auth.exception;

import com.example.security.shared.exception.GlobalException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class InvalidRefreshTokenException extends GlobalException {
    private String message;

    @Override
    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
