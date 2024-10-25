package com.example.backend.service;

import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarHealthServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarHealthRepository carHealthRepository;

    @Test
    void getAllCars_ShouldReturnCarDtoList() throws Exception {
        // GIVEN
        Car car1 = new Car("1", "Model S", 2020, "VIN123");
        Car car2 = new Car("2", "Model X", 2021, "VIN456");
        when(carHealthRepository.findAll()).thenReturn(List.of(car1, car2));
        // WHEN & THEN
        mockMvc.perform(get("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].model").value("Model S"));
        verify(carHealthRepository, times(1)).findAll();
    }

    @Test
    void getCarById_ShouldReturnCarDto_WhenCarExists() throws Exception {
        // GIVEN
        String carId = "1";
        Car car = new Car(carId, "Model S", 2020, "VIN123");
        when(carHealthRepository.findById(carId)).thenReturn(Optional.of(car));
        // WHEN & THEN
        mockMvc.perform(get("/api/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId))
                .andExpect(jsonPath("$.model").value("Model S"));
        verify(carHealthRepository, times(1)).findById(carId);
    }

    @Test
    void getCarById_ShouldThrowException_WhenCarNotFound() throws Exception {
        // GIVEN
        String carId = "1";
        when(carHealthRepository.findById(carId)).thenReturn(Optional.empty());
        // WHEN & THEN
        mockMvc.perform(get("/api/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(carHealthRepository, times(1)).findById(carId);
    }

    @Test
    void createCar_ShouldSaveAndReturnCarDto() throws Exception {
        // GIVEN
        Car car = new Car("1", "Model S", 2020, "VIN123");
        when(carHealthRepository.save(any(Car.class))).thenReturn(car);
        // WHEN & THEN
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    	"id":"1",
                                    	"model":"Model S",
                                    	"year":2020,
                                    	"vin":"VIN123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Model S"));
        verify(carHealthRepository, times(1)).save(any(Car.class));
    }

    @Test
    void deleteCarById_ShouldDeleteCar_WhenCarExists() throws Exception {
        // GIVEN
        String carId = "1";
        doReturn(Optional.of(new Car(carId, "Model S", 2020, "VIN123"))).when(carHealthRepository).findById(carId);
        doNothing().when(carHealthRepository).deleteById(carId);
        // WHEN & THEN
        mockMvc.perform(delete("/api/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(carHealthRepository, times(1)).findById(carId);
        verify(carHealthRepository, times(1)).deleteById(carId);
    }

    @Test
    void deleteCarById_ShouldReturn404_WhenCarNotFound() throws Exception {
        // GIVEN
        String carId = "1";
        when(carHealthRepository.findById(carId)).thenReturn(Optional.empty());
        // WHEN & THEN
        mockMvc.perform(delete("/api/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(carHealthRepository, times(1)).findById(carId);
        verify(carHealthRepository, never()).deleteById(carId);
    }
}
