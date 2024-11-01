package com.example.backend.service;

import com.example.backend.dto.CarDto;
import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarHealthServiceTest {

    @Autowired
    private CarHealthService carHealthService;

    @Autowired
    private CarHealthRepository carHealthRepository;

    @BeforeEach
    void setUp() {
        carHealthRepository.deleteAll();
    }

    @Test
    void getAllCars_shouldReturnCarList() {
        Car car1 = new Car("1", "Model X", 2020, "VIN123", 10000);
        Car car2 = new Car("2", "Model Y", 2021, "VIN456", 20000);
        carHealthRepository.saveAll(List.of(car1, car2));
        List<CarDto> result = carHealthService.getAllCars();
        assertEquals(2, result.size());
        assertEquals("Model X", result.get(0).model());
        assertEquals("VIN456", result.get(1).vin());
    }

    @Test
    void getCarById_shouldReturnCar_whenCarExists() {
        Car car = new Car("1", "Model X", 2020, "VIN123", 10000);
        carHealthRepository.save(car);
        CarDto result = carHealthService.getCarById("1");
        assertEquals("Model X", result.model());
        assertEquals("VIN123", result.vin());
        assertEquals(10000, result.currentMileage());
    }

    @Test
    void getCarById_shouldThrowException_whenCarNotFound() {
        assertThrows(NoSuchElementException.class, () -> carHealthService.getCarById("999"));
    }

    @Test
    void createCar_shouldSaveAndReturnCar() {
        CarDto carDto = new CarDto("3", "Model Z", 2022, "VIN789", 10000);
        CarDto result = carHealthService.createCar(carDto);
        assertEquals("Model Z", result.model());
        assertEquals("VIN789", result.vin());
        assertEquals(10000, result.currentMileage());
        Optional<Car> savedCar = carHealthRepository.findById("3");
        assertTrue(savedCar.isPresent());
        assertEquals("Model Z", savedCar.get().model());
    }

    @Test
    void updateCar_shouldUpdateAndReturnUpdatedCar_whenCarExists() {
        Car existingCar = new Car("1", "Model X", 2020, "VIN123", 10000);
        carHealthRepository.save(existingCar);
        CarDto updatedCarDto = new CarDto("1", "Model S", 2021, "VIN1234", 20000);
        CarDto result = carHealthService.updateCar("1", updatedCarDto);
        assertEquals("Model S", result.model());
        assertEquals("VIN1234", result.vin());
        assertEquals(20000, result.currentMileage());
        Optional<Car> updatedCar = carHealthRepository.findById("1");
        assertTrue(updatedCar.isPresent());
        assertEquals("Model S", updatedCar.get().model());
    }

    @Test
    void updateCar_shouldThrowException_whenCarNotFound() {
        CarDto updatedCarDto = new CarDto("999", "Model Z", 2022, "VIN789", 10000);
        assertThrows(NoSuchElementException.class, () -> carHealthService.updateCar("999", updatedCarDto));
    }

    @Test
    void deleteCarById_shouldDeleteCar_whenCarExists() {
        Car existingCar = new Car("1", "Model X", 2020, "VIN123", 10000);
        carHealthRepository.save(existingCar);
        carHealthService.deleteCarById("1");
        Optional<Car> deletedCar = carHealthRepository.findById("1");
        assertTrue(deletedCar.isEmpty());
    }

    @Test
    void deleteCarById_shouldThrowException_whenCarNotFound() {
        assertThrows(NoSuchElementException.class, () -> carHealthService.deleteCarById("999"));
    }
}
