package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void testCarConstructor() {
        Car car = new Car("1", "user123", "Toyota Camry", 2020, "1HGBH41JXMN109186", 15000);
        assertNotNull(car);
        assertEquals("1", car.id());
        assertEquals("user123", car.userId());
        assertEquals("Toyota Camry", car.model());
        assertEquals(2020, car.year());
        assertEquals("1HGBH41JXMN109186", car.vin());
        assertEquals(15000, car.currentMileage());
    }
}