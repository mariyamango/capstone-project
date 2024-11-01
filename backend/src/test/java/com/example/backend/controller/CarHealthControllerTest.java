package com.example.backend.controller;

import com.example.backend.dto.CarDto;
import com.example.backend.dto.CreateCarRequest;
import com.example.backend.dto.CreateWorkRequest;
import com.example.backend.dto.WorkDto;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import com.example.backend.service.WorkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarHealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarHealthService carHealthService;

    @MockBean
    private WorkService workService;

    @MockBean
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCars_shouldGetAllCars() throws Exception {
        //GIVEN
        List<CarDto> mockCars = Arrays.asList(
                new CarDto("1", "Model X", 2020, "VIN123",10000),
                new CarDto("2", "Model Y", 2021, "VIN456",20000)
        );
        when(carHealthService.getAllCars()).thenReturn(mockCars);
        //WHEN THEN
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockCars.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Model X"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].vin").value("VIN123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currentMileage").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value("Model Y"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(2021))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].vin").value("VIN456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currentMileage").value(20000));
        verify(carHealthService, times(1)).getAllCars();
    }

    @Test
    void getCarById_shouldReturnCar() throws Exception {
        //GIVEN
        CarDto mockCar = new CarDto("1", "Model X", 2020, "VIN123",10000);
        when(carHealthService.getCarById("1")).thenReturn(mockCar);
        //WHEN THEN
        mockMvc.perform(get("/api/cars/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model X"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vin").value("VIN123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentMileage").value(10000));
        verify(carHealthService, times(1)).getCarById("1");
    }

    @Test
    void createCar_shouldCreateNewCar() throws Exception {
        //GIVEN
        CreateCarRequest createCarRequest = new CreateCarRequest("Model Z", 2022, "VIN789",10000);
        CarDto mockCar = new CarDto("generated-id", "Model Z", 2022, "VIN789",20000);
        when(idGeneratorService.generateId()).thenReturn("generated-id");
        when(carHealthService.createCar(any(CarDto.class))).thenReturn(mockCar);
        //WHEN THEN
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("generated-id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model Z"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2022))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vin").value("VIN789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentMileage").value(20000));

        verify(idGeneratorService, times(1)).generateId();
        verify(carHealthService, times(1)).createCar(any(CarDto.class));
    }

    @Test
    void updateCar_shouldUpdateExistingCar() throws Exception {
        //GIVEN
        String id = "123";
        CarDto carDto = new CarDto("123", "Model S", 2023, "VIN123456",10000);
        when(carHealthService.updateCar(eq(id), any(CarDto.class))).thenReturn(carDto);
        //WHEN THEN
        mockMvc.perform(put("/api/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.model").value("Model S"))
                .andExpect(jsonPath("$.year").value("2023"))
                .andExpect(jsonPath("$.vin").value("VIN123456"))
                .andExpect(jsonPath("$.currentMileage").value(10000));
    }

    @Test
    void updateCar_shouldThrowException() throws Exception {
        //GIVEN
        String id = "123";
        CarDto carDto = new CarDto("123", "Model S", 2023, "VIN123456",10000);
        when(carHealthService.updateCar(eq(id), any(CarDto.class)))
                .thenThrow(new NoSuchElementException("Car not found"));
        //WHEN THEN
        mockMvc.perform(put("/api/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_shouldDeleteExistingCar() throws Exception {
        //GIVEN
        String carId = "1";
        Mockito.doNothing().when(carHealthService).deleteCarById(carId);
        //WHEN THEN
        mockMvc.perform(delete("/api/cars/{id}", carId))
                .andExpect(status().isNoContent());
        verify(carHealthService, times(1)).deleteCarById(carId);
    }

    @Test
    void deleteCar_shouldThrowNoSuchElementException() throws Exception {
        //GIVEN
        String nonExistentCarId = "999";
        doThrow(new NoSuchElementException("Car not found with id: " + nonExistentCarId))
                .when(carHealthService).deleteCarById(nonExistentCarId);
        //WHEN THEN
        mockMvc.perform(delete("/api/cars/{id}", nonExistentCarId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Car not found with id: " + nonExistentCarId));
        verify(carHealthService, times(1)).deleteCarById(nonExistentCarId);
    }

    @Test
    void work_shouldReturnListOfWorkDtos() throws Exception {
        // GIVEN
        String carId = "1";
        List<WorkDto> works = Arrays.asList(
                new WorkDto("1", carId, "Oil Change", 5000, LocalDate.of(2023,1,10), 50.0),
                new WorkDto("2", carId, "Tire Replacement", 8000, LocalDate.of(2023,2,15), 120.0)
        );
        when(workService.getAllWorksByCarId(carId)).thenReturn(works);

        // WHEN THEN
        mockMvc.perform(get("/api/works/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].carId").value(carId))
                .andExpect(jsonPath("$[0].type").value("Oil Change"))
                .andExpect(jsonPath("$[0].mileage").value(5000))
                .andExpect(jsonPath("$[0].date").value("2023-01-10"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].carId").value(carId))
                .andExpect(jsonPath("$[1].type").value("Tire Replacement"))
                .andExpect(jsonPath("$[1].mileage").value(8000))
                .andExpect(jsonPath("$[1].date").value("2023-02-15"))
                .andExpect(jsonPath("$[1].price").value(120.0));

        verify(workService, times(1)).getAllWorksByCarId(carId);
    }

    @Test
    void work_shouldReturnEmptyListWhenNoWorksFound() throws Exception {
        // GIVEN
        String carId = "2";
        when(workService.getAllWorksByCarId(carId)).thenReturn(Collections.emptyList());

        // WHEN THEN
        mockMvc.perform(get("/api/works/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(workService, times(1)).getAllWorksByCarId(carId);
    }

    @Test
    void createWork_shouldCreateNewWork() throws Exception {
        //GIVEN
        CreateWorkRequest createWorkRequest = new CreateWorkRequest("generated-carId","Tires change",10000, LocalDate.of(2023,1,10), 50.0);
        WorkDto mockWork = new WorkDto("generated-id", "generated-carId","Tires change",10000, LocalDate.of(2023,1,10), 50.0);
        when(idGeneratorService.generateId()).thenReturn("generated-id");
        when(workService.createWork(any(WorkDto.class))).thenReturn(mockWork);
        //WHEN THEN
        mockMvc.perform(post("/api/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWorkRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("generated-id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value("generated-carId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Tires change"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-01-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("50.0"));

        verify(idGeneratorService, times(1)).generateId();
        verify(workService, times(1)).createWork(any(WorkDto.class));
    }

    @Test
    void updateWork_shouldUpdateExistingWork() throws Exception {
        //GIVEN
        String id = "generated-id";
        WorkDto workDto = new WorkDto("generated-id","generated-carId","Tires change",10000, LocalDate.of(2023,1,10), 50.0);
        when(workService.updateWork(eq(id), any(WorkDto.class))).thenReturn(workDto);
        //WHEN THEN
        mockMvc.perform(put("/api/works/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("generated-id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value("generated-carId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Tires change"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-01-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("50.0"));
    }

    @Test
    void updateWork_shouldThrowException() throws Exception {
        //GIVEN
        String id = "generated-id";
        WorkDto workDto = new WorkDto("generated-id","generated-carId","Tires change",10000, LocalDate.of(2023,1,10), 50.0);
        when(workService.updateWork(eq(id), any(WorkDto.class)))
                .thenThrow(new NoSuchElementException("Work not found"));
        //WHEN THEN
        mockMvc.perform(put("/api/works/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWork_shouldDeleteExistingWork() throws Exception {
        //GIVEN
        String id = "1";
        Mockito.doNothing().when(workService).deleteWorkById(id);
        //WHEN THEN
        mockMvc.perform(delete("/api/works/{id}", id))
                .andExpect(status().isNoContent());
        verify(workService, times(1)).deleteWorkById(id);
    }

    @Test
    void deleteWork_shouldThrowNoSuchElementException() throws Exception {
        //GIVEN
        String nonExistentWorkId = "not-existent-id";
        doThrow(new NoSuchElementException("Work not found with id: " + nonExistentWorkId))
                .when(workService).deleteWorkById(nonExistentWorkId);
        //WHEN THEN
        mockMvc.perform(delete("/api/works/{id}", nonExistentWorkId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Work not found with id: " + nonExistentWorkId));
        verify(workService, times(1)).deleteWorkById(nonExistentWorkId);
    }
}