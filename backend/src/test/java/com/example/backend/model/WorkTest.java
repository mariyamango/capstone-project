package com.example.backend.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkTest {

    @Test
    void createWork_shouldInitializeFieldsCorrectly() {
        // GIVEN
        String id = "1";
        String carId = "car-1";
        String type = "Oil Change";
        int mileage = 5000;
        LocalDate date = LocalDate.of(2023, 1, 10);
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
