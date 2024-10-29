package com.example.backend.service;

import com.example.backend.dto.CarDto;
import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarHealthServiceTest {

    @Mock
    private CarHealthRepository carHealthRepository;

    @InjectMocks
    private CarHealthService carHealthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCars_shouldReturnCarList() {
        List<Car> cars = List.of(
                new Car("1", "Model X", 2020, "VIN123"),
                new Car("2", "Model Y", 2021, "VIN456")
        );
        when(carHealthRepository.findAll()).thenReturn(cars);
        List<CarDto> result = carHealthService.getAllCars();
        assertEquals(2, result.size());
        assertEquals("Model X", result.get(0).model());
        assertEquals("VIN456", result.get(1).vin());
        verify(carHealthRepository, times(1)).findAll();
    }

    @Test
    void getCarById_shouldReturnCar_whenCarExists() {
        Car car = new Car("1", "Model X", 2020, "VIN123");
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(car));
        CarDto result = carHealthService.getCarById("1");
        assertEquals("Model X", result.model());
        assertEquals("VIN123", result.vin());
        verify(carHealthRepository, times(1)).findById("1");
    }

    @Test
    void getCarById_shouldThrowException_whenCarNotFound() {
        when(carHealthRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> carHealthService.getCarById("999"));
        verify(carHealthRepository, times(1)).findById("999");
    }

    @Test
    void createCar_shouldSaveAndReturnCar() {
        CarDto carDto = new CarDto("3", "Model Z", 2022, "VIN789");
        Car car = new Car("3", "Model Z", 2022, "VIN789");
        when(carHealthRepository.save(any(Car.class))).thenReturn(car);
        CarDto result = carHealthService.createCar(carDto);
        assertEquals("Model Z", result.model());
        assertEquals("VIN789", result.vin());
        verify(carHealthRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_shouldUpdateAndReturnUpdatedCar_whenCarExists() {
        Car existingCar = new Car("1", "Model X", 2020, "VIN123");
        CarDto updatedCarDto = new CarDto("1", "Model S", 2021, "VIN1234");
        Car updatedCar = new Car("1", "Model S", 2021, "VIN1234");
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(existingCar));
        when(carHealthRepository.save(any(Car.class))).thenReturn(updatedCar);
        CarDto result = carHealthService.updateCar("1", updatedCarDto);
        assertEquals("Model S", result.model());
        assertEquals("VIN1234", result.vin());
        verify(carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_shouldThrowException_whenCarNotFound() {
        CarDto updatedCarDto = new CarDto("999", "Model Z", 2022, "VIN789");
        when(carHealthRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> carHealthService.updateCar("999", updatedCarDto));
        verify(carHealthRepository, times(1)).findById("999");
        verify(carHealthRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteCarById_shouldDeleteCar_whenCarExists() {
        Car existingCar = new Car("1", "Model X", 2020, "VIN123");
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(existingCar));
        carHealthService.deleteCarById("1");
        verify(carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteCarById_shouldThrowException_whenCarNotFound() {
        when(carHealthRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> carHealthService.deleteCarById("999"));
        verify(carHealthRepository, times(1)).findById("999");
        verify(carHealthRepository, never()).deleteById(anyString());
    }
}