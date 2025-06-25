package com.example.security.example.controller;

import com.example.security.shared.model.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExampleHelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("Hello " + user.getUsername());
    }
}
