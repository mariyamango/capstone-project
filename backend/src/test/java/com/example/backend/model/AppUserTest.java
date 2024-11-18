package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppUserTest {

    @Test
    void testAppUserCreation() {
        // GIVEN
        String id = "123";
        String username = "john_doe";
        String avatarUrl = "http://example.com/avatar.jpg";
        String role = "user";
        // WHEN
        AppUser appUser = AppUser.builder()
                .id(id)
                .username(username)
                .avatarUrl(avatarUrl)
                .role(role)
                .build();
        // THEN
        assertEquals(id, appUser.id());
        assertEquals(username, appUser.username());
        assertEquals(avatarUrl, appUser.avatarUrl());
        assertEquals(role, appUser.role());
    }

    @Test
    void testEqualsAndHashCode() {
        // GIVEN
        AppUser appUser1 = AppUser.builder()
                .id("123")
                .username("john_doe")
                .avatarUrl("http://example.com/avatar1.jpg")
                .role("user")
                .build();
        AppUser appUser2 = AppUser.builder()
                .id("123")
                .username("john_doe")
                .avatarUrl("http://example.com/avatar1.jpg")
                .role("user")
                .build();
        // WHEN AND THEN
        assertEquals(appUser1, appUser2);
        assertEquals(appUser1.hashCode(), appUser2.hashCode());
    }

    @Test
    void testNotEqual() {
        // GIVEN
        AppUser appUser1 = AppUser.builder()
                .id("123")
                .username("john_doe")
                .avatarUrl("http://example.com/avatar1.jpg")
                .role("user")
                .build();
        AppUser appUser2 = AppUser.builder()
                .id("456")
                .username("jane_doe")
                .avatarUrl("http://example.com/avatar2.jpg")
                .role("admin")
                .build();
        // WHEN AND THEN
        assertNotEquals(appUser1, appUser2);
        assertNotEquals(appUser1.hashCode(), appUser2.hashCode());
    }

    @Test
    void testToString() {
        // GIVEN
        AppUser appUser = AppUser.builder()
                .id("123")
                .username("john_doe")
                .avatarUrl("http://example.com/avatar.jpg")
                .role("user")
                .build();
        // WHEN
        String expectedString = "AppUser[id=123, username=john_doe, avatarUrl=http://example.com/avatar.jpg, role=user]";
        // THEN
        assertEquals(expectedString, appUser.toString());
    }
}