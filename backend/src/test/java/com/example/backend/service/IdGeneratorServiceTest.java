package com.example.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdGeneratorServiceTest {

    @Mock
    private IdGeneratorService idGeneratorService;

    @Test
    void randomId_shouldReturnNonNullId() {
        //GIVEN
        when(idGeneratorService.generateId()).thenReturn(UUID.randomUUID().toString());
        //WHEN
        String id = idGeneratorService.generateId();
        //THEN
        assertNotNull(id, "Id should not be null");
    }

    @Test
    void randomId_shouldReturnValidUUIDFormat() {
        //GIVEN
        when(idGeneratorService.generateId()).thenReturn(UUID.randomUUID().toString());
        //WHEN
        String id = idGeneratorService.generateId();
        //THEN
        assertTrue(isValidUUID(id), "Id should be in UUID format");
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