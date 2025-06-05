package com.example.security.shared.exception.adviser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private List<ExceptionField> messages;

    @Data
    @AllArgsConstructor
    public static class ExceptionField {
        private String field;
        private String message;
    }
}
