package com.example.security.shared.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableUtils {
    public static <T> List<T> safeImmutableList(List<T> list) {
        return list == null ? List.of() : Collections.unmodifiableList(list);
    }
}
