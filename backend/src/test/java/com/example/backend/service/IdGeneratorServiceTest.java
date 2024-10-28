package com.example.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IdGeneratorServiceTest {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Test
    void randomId_shouldReturnNonNullId() {
        String id = idGeneratorService.generateId();
        assertNotNull(id, "ID should not be null");
    }

    @Test
    void randomId_shouldReturnValidUUIDFormat() {
        String id = idGeneratorService.generateId();
        assertTrue(isValidUUID(id), "ID should be in UUID format");
    }

    private boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}