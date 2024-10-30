package com.example.backend.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createWork_shouldInitializeFieldsCorrectly() {
        // GIVEN
        String id = "1";
        String carId = "car-1";
        String type = "Oil Change";
        int mileage = 5000;
        LocalDateTime date = LocalDateTime.of(2023, 1, 10, 0, 0);
        double price = 50.0;
        // WHEN
        Work work = new Work(id, carId, type, mileage, date, price);
        // THEN
        assertEquals(id, work.id());
        assertEquals(carId, work.carId());
        assertEquals(type, work.type());
        assertEquals(mileage, work.mileage());
        assertEquals(date, work.date());
        assertEquals(price, work.price());
    }
}
