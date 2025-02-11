package com.example.security.auth;

import com.example.security.IntegrationTestBase;
import com.example.security.TestUserHelper;
import com.example.security.auth.api.protocol.request.RegistryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerRegistryTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserHelper helper;

    @BeforeEach
    void setup() {
        helper.cleanUsers();
    }

    @Test
    void shouldRegisterUserSuccessfullyTest() throws Exception {
        // given
        var request = new RegistryRequest("testUsername", "strong password");

        // when and then
        mockMvc.perform(post("/api/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldRegisterUserFailWhenGotPasswordNullTest() throws Exception {
        // given
        var request = new RegistryRequest("testUsername", null);

        // when and then
        mockMvc.perform(post("/api/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("must not be blank"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "a1", "a12", "a123", "a1234"})
    void shouldRegisterUserFailWhenGotTooShortPasswordTest(String password) throws Exception {
        // given
        var request = new RegistryRequest("testUsername", password);

        // when and then
        mockMvc.perform(post("/api/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password must have at least 6 characters"));
    }

    @Test
    void shouldRegisterUserFailWhenUsernameAlreadyExistsTest() throws Exception {
        // given
        var request = new RegistryRequest("testUsername", "strong password");
        helper.registerUser(request.username(), request.password());

        // when and then
        mockMvc.perform(post("/api/auth/registry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("User with username \""+ request.username() +"\" is already taken."));
    }
}
