package com.example.security.auth;

import com.example.security.IntegrationTestBase;
import com.example.security.TestUserHelper;
import com.example.security.auth.controller.protocol.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerLoginTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserHelper helper;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.refresh-token.expiration-time}")
    private long refreshTokenExpiration;

    @BeforeEach
    void setup() {
        helper.cleanUsers();
    }

    @Test
    void shouldLoginSuccessfullyTest() throws Exception {
        // given
        var request = new LoginRequest("testUsername", "strong password");
        helper.registerUser(request.username(), request.password());

        // when and then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(request.username()))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessTokenExpiresIn").value(jwtExpiration))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshTokenExpiresIn").value(refreshTokenExpiration));
    }

    @Test
    void shouldLoginWhenUsernameNoExistsTest() throws Exception {
        // given
        var request = new LoginRequest("testUsername", "strong password");
        helper.registerUser("not_" + request.username(), request.password());

        // when and then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    void shouldLoginWhenIncorrectPasswordTest() throws Exception {
        // given
        var request = new LoginRequest("testUsername", "strong password");
        helper.registerUser(request.username(), "other " + request.password());

        // when and then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }
}
