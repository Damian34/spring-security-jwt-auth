package com.example.security.auth;

import com.example.security.TestContainerInitializer;
import com.example.security.TestUserHelper;
import com.example.security.auth.api.protocol.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class AuthControllerLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

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
