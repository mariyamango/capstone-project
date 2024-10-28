package com.example.backend.controller;

import com.example.backend.dto.CarDto;
import com.example.backend.dto.CreateCarRequest;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarHealthControllerTest {

    @Mock
    private CarHealthService carHealthService;

    @Mock
    private IdGeneratorService idGeneratorService;

    @InjectMocks
    private CarHealthController carHealthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCars() {
        List<CarDto> mockCars = Arrays.asList(
                new CarDto("1", "Model X", 2020, "VIN123"),
                new CarDto("2", "Model Y", 2021, "VIN456")
        );
        when(carHealthService.getAllCars()).thenReturn(mockCars);

        List<CarDto> result = carHealthController.cars();

        assertEquals(mockCars.size(), result.size());
        assertEquals(mockCars, result);
        verify(carHealthService, times(1)).getAllCars();
    }

    @Test
    void testGetCarById() {
        CarDto mockCar = new CarDto("1", "Model X", 2020, "VIN123");
        when(carHealthService.getCarById("1")).thenReturn(mockCar);

        CarDto result = carHealthController.car("1");

        assertEquals(mockCar, result);
        verify(carHealthService, times(1)).getCarById("1");
    }

    @Test
    void testCreateCar() {
        CreateCarRequest createCarRequest = new CreateCarRequest("Model Z", 2022, "VIN789");
        CarDto mockCar = new CarDto("generated-id", "Model Z", 2022, "VIN789");
        when(idGeneratorService.generateId()).thenReturn("generated-id");
        when(carHealthService.createCar(any(CarDto.class))).thenReturn(mockCar);

        CarDto result = carHealthController.createCar(createCarRequest);

        assertEquals(mockCar, result);
        verify(idGeneratorService, times(1)).generateId();
        verify(carHealthService, times(1)).createCar(any(CarDto.class));
    }

    @Test
    void testDeleteCar() {
        String carId = "1";
        doNothing().when(carHealthService).deleteCarById(carId);

        ResponseEntity<Void> response = carHealthController.deleteCar(carId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carHealthService, times(1)).deleteCarById(carId);
    }
}
