package com.example.backend.service;

import com.example.backend.dto.CarDto;
import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarHealthServiceTest {

    @Mock
    private CarHealthRepository carHealthRepository = mock(CarHealthRepository.class);

    @InjectMocks
    private CarHealthService carHealthService;

    @Test
    void getAllCars_shouldReturnCarList() {
        //GIVEN
        Car car1 = new Car("1", "Model X", 2020, "VIN123", 10000);
        Car car2 = new Car("2", "Model Y", 2021, "VIN456", 20000);
        when(carHealthRepository.findAll()).thenReturn(List.of(car1, car2));
        //WHEN
        List<CarDto> result = carHealthService.getAllCars();
        //THEN
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("Model X", result.get(0).model());
        assertEquals(2020, result.get(0).year());
        assertEquals("VIN123", result.get(0).vin());
        assertEquals(10000, result.get(0).currentMileage());
        assertEquals("2", result.get(1).id());
        assertEquals("Model Y", result.get(1).model());
        assertEquals(2021, result.get(1).year());
        assertEquals("VIN456", result.get(1).vin());
        assertEquals(20000, result.get(1).currentMileage());
        verify(carHealthRepository, times(1)).findAll();
    }

    @Test
    void getCarById_shouldReturnCar_whenCarExists() {
        //GIVEN
        Car car = new Car("1", "Model X", 2020, "VIN123", 10000);
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(car));
        //WHEN
        CarDto result = carHealthService.getCarById("1");
        //THEN
        assertEquals("Model X", result.model());
        assertEquals(2020, result.year());
        assertEquals("VIN123", result.vin());
        assertEquals(10000, result.currentMileage());
        verify(carHealthRepository, times(1)).findById("1");
    }

    @Test
    void getCarById_shouldThrowException_whenCarNotFound() {
        //GIVEN
        when(carHealthRepository.findById("1")).thenReturn(Optional.empty());
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> carHealthService.getCarById("1"));
        verify(carHealthRepository, times(1)).findById("1");
    }

    @Test
    void createCar_shouldSaveAndReturnCar() {
        //GIVEN
        Car car = new Car("1", "Model X", 2020, "VIN123", 10000);
        CarDto carDto = new CarDto("1", "Model X", 2020, "VIN123", 10000);
        when(carHealthRepository.save(car)).thenReturn(car);
        //WHEN THEN
        CarDto result = carHealthService.createCar(carDto);
        assertEquals("Model X", result.model());
        assertEquals(2020, result.year());
        assertEquals("VIN123", result.vin());
        assertEquals(10000, result.currentMileage());
        verify(carHealthRepository, times(1)).save(car);
    }

    @Test
    void updateCar_shouldUpdateAndReturnUpdatedCar_whenCarExists() {
        //GIVEN
        Car existingCar = new Car("1", "Model X", 2020, "VIN123", 10000);
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(existingCar));
        Car updatedCar = new Car("1", "Model S", 2021, "VIN1234", 20000);
        CarDto updatedCarDto = new CarDto("1", "Model S", 2021, "VIN1234", 20000);
        //WHEN
        CarDto result = carHealthService.updateCar("1", updatedCarDto);
        //THEN
        assertEquals("Model S", result.model());
        assertEquals(2021, result.year());
        assertEquals("VIN1234", result.vin());
        assertEquals(20000, result.currentMileage());
        verify(carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(1)).save(updatedCar);
    }

    @Test
    void updateCar_shouldThrowException_whenCarNotFound() {
        //GIVEN
        when(carHealthRepository.findById("1")).thenReturn(Optional.empty());
        CarDto updatedCarDto = new CarDto("1", "Model Z", 2022, "VIN789", 10000);
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> carHealthService.updateCar("1", updatedCarDto));
        verify(carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(0)).save(any(Car.class));
    }

    @Test
    void deleteCarById_shouldDeleteCar_whenCarExists() {
        //GIVEN
        Car existingCar = new Car("1", "Model X", 2020, "VIN123", 10000);
        when(carHealthRepository.findById("1")).thenReturn(Optional.of(existingCar));
        //WHEN
        carHealthService.deleteCarById("1");
        //THEN
        verify( carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(0)).delete(existingCar);
    }

    @Test
    void deleteCarById_shouldThrowException_whenCarNotFound() {
        //GIVEN
        when(carHealthRepository.findById("1")).thenReturn(Optional.empty());
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> carHealthService.deleteCarById("1"));
        verify(carHealthRepository, times(1)).findById("1");
        verify(carHealthRepository, times(0)).delete(any(Car.class));
    }
}
