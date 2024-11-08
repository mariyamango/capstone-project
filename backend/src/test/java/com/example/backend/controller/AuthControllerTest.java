package com.example.backend.controller;

import com.example.backend.model.AppUser;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getMe_whenNotLoggedIn_return401() throws Exception {
        // WHEN
        mockMvc.perform(get("/api/auth/me"))
                // THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMe_whenLoggedIn_returnUsername() throws Exception {
        // GIVEN
        var appUser = AppUser.builder()
                .id("John")
                .username("John")
                .avatarUrl("http://example.com/avatar.jpg")
                .role("USER")
                .build();
        when(userRepository.findById("John")).thenReturn(Optional.of(appUser));
        // WHEN
        mockMvc.perform(get("/api/auth/me")
                        .with(oidcLogin().idToken(token -> token
                                .subject("John"))))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("John"))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.avatarUrl").value("http://example.com/avatar.jpg"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void logout_whenLoggingOut_returnOk() throws Exception {
        // WHEN
        mockMvc.perform(get("/api/auth/logout")
                        .with(oidcLogin().userInfoToken(token -> token
                                .claim("login", "John"))))
                // THEN
                .andExpect(status().isOk());
    }

    @Test
    void logout_whenNotLoggedIn_return401() throws Exception {
        // WHEN
        mockMvc.perform(get("/api/auth/logout"))
                // THEN
                .andExpect(status().isOk());
    }
}