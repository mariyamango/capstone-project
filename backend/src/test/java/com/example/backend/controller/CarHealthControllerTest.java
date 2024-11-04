package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.repository.CarHealthRepository;
import com.example.backend.repository.WorkRepository;
import com.example.backend.repository.WorkTypeRepository;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import com.example.backend.service.WorkService;
import com.example.backend.service.WorkTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarHealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarHealthRepository carHealthRepository;
    @Autowired
    private CarHealthService carHealthService;

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private WorkService workService;

    @Autowired
    private WorkTypeRepository workTypeRepository;
    @Autowired
    private WorkTypeService workTypeService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @DirtiesContext
    @Test
    void getAllCars_shouldGetAllCars() throws Exception {
        //GIVEN
        CarDto car1 = new CarDto("1", "Model X", 2020, "VIN123", 10000);
        CarDto car2 = new CarDto("2", "Model Y", 2021, "VIN456", 20000);
        carHealthService.createCar(car1);
        carHealthService.createCar(car2);
        //WHEN THEN
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
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
    }

    @DirtiesContext
    @Test
    void getCarById_shouldReturnCar() throws Exception {
        //GIVEN
        CarDto car1 = new CarDto("1", "Model X", 2020, "VIN123", 10000);
        carHealthService.createCar(car1);
        //WHEN THEN
        mockMvc.perform(get("/api/cars/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model X"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vin").value("VIN123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentMileage").value(10000));
    }

    @DirtiesContext
    @Test
    void createCar_shouldCreateNewCar() throws Exception {
        //GIVEN
        CreateCarRequest createCarRequest = new CreateCarRequest("Model Z", 2022, "VIN789", 10000);
        //WHEN THEN
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model Z"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2022))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vin").value("VIN789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentMileage").value(10000));
    }

    @DirtiesContext
    @Test
    void updateCar_shouldUpdateExistingCar() throws Exception {
        //GIVEN
        String id = "123";
        CarDto carDto = new CarDto(id, "Model S", 2023, "VIN123456", 10000);
        carHealthService.createCar(carDto);
        CarDto updatedCarDto = new CarDto(id, "Model X", 2021, "VIN456", 20000);
        //WHEN THEN
        mockMvc.perform(put("/api/cars/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCarDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.model").value("Model X"))
                .andExpect(jsonPath("$.year").value("2021"))
                .andExpect(jsonPath("$.vin").value("VIN456"))
                .andExpect(jsonPath("$.currentMileage").value(20000));
    }

    @DirtiesContext
    @Test
    void updateCar_shouldThrowException() throws Exception {
        //GIVEN
        String id = "123";
        CarDto carDto = new CarDto(id, "Model S", 2023, "VIN123456", 10000);
        carHealthService.createCar(carDto);
        CarDto updatedCarDto = new CarDto(id, "Model X", 2021, "VIN456", 20000);
        //WHEN THEN
        mockMvc.perform(put("/api/cars/{id}", "124")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCarDto)))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext
    @Test
    void deleteCar_shouldDeleteExistingCar() throws Exception {
        //GIVEN
        String id = "123";
        CarDto carDto = new CarDto(id, "Model S", 2023, "VIN123456", 10000);
        carHealthService.createCar(carDto);
        //WHEN THEN
        mockMvc.perform(delete("/api/cars/{id}", id))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext
    @Test
    void deleteCar_shouldThrowNoSuchElementException() throws Exception {
        //GIVEN
        String nonExistentCarId = "999";
        //WHEN THEN
        mockMvc.perform(delete("/api/cars/{id}", nonExistentCarId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext
    @Test
    void work_shouldReturnListOfWorkDtos() throws Exception {
        // GIVEN
        String carId = "123";
        WorkDto workDto1 = new WorkDto("1", carId, "workTypeId", "Oil Change", 5000, LocalDate.of(2023, 1, 10), 50.0);
        WorkDto workDto2 = new WorkDto("2", carId, "workTypeId", "Tire Replacement", 8000, LocalDate.of(2023, 2, 15), 120.0);
        workService.createWork(workDto1);
        workService.createWork(workDto2);
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
    }

    @DirtiesContext
    @Test
    void work_shouldReturnEmptyListWhenNoWorksFound() throws Exception {
        // GIVEN
        String carId = "123";
        // WHEN THEN
        mockMvc.perform(get("/api/works/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DirtiesContext
    @Test
    void createWork_shouldCreateNewWork() throws Exception {
        //GIVEN
        CreateWorkRequest createWorkRequest = new CreateWorkRequest("generated-carId", "workTypeId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        WorkDto workDto = new WorkDto("generated-id", "generated-carId", "workTypeId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        workService.createWork(workDto);
        //WHEN THEN
        mockMvc.perform(post("/api/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWorkRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value("generated-carId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Tires change"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-01-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("50.0"));
    }

    @DirtiesContext
    @Test
    void updateWork_shouldUpdateExistingWork() throws Exception {
        //GIVEN
        String id = "generated-id";
        WorkDto workDto = new WorkDto(id, "generated-carId", "workTypeId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        workService.createWork(workDto);
        WorkDto updatedWorkDto = new WorkDto(id, "generated-carId", "updatedWorkTypeId", "Oil change", 10000, LocalDate.of(2023, 1, 12), 55.0);
        //WHEN THEN
        mockMvc.perform(put("/api/works/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWorkDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId").value("generated-carId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workTypeId").value("updatedWorkTypeId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Oil change"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mileage").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-01-12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("55.0"));
    }

    @DirtiesContext
    @Test
    void updateWork_shouldThrowException() throws Exception {
        //GIVEN
        String id = "generated-id";
        WorkDto workDto = new WorkDto(id, "generated-carId", "workTypeId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        //WHEN THEN
        mockMvc.perform(put("/api/works/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workDto)))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext
    @Test
    void deleteWork_shouldDeleteExistingWork() throws Exception {
        //GIVEN
        String id = "generated-id";
        WorkDto workDto = new WorkDto(id, "generated-carId", "workTypeId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        workService.createWork(workDto);
        //WHEN THEN
        mockMvc.perform(delete("/api/works/{id}", id))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext
    @Test
    void deleteWork_shouldThrowNoSuchElementException() throws Exception {
        //GIVEN
        String nonExistentWorkId = "not-existent-id";
        //WHEN THEN
        mockMvc.perform(delete("/api/works/{id}", nonExistentWorkId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext
    @Test
    void getAllWorkTypes_shouldGetAllWorkTypes() throws Exception {
        //GIVEN
        WorkTypeDto workTypeDto1 = new WorkTypeDto("1", "Work Type 1", 10000, 300);
        WorkTypeDto workTypeDto2 = new WorkTypeDto("2", "Work Type 2", 20000, 250);
        workTypeService.createWorkType(workTypeDto1);
        workTypeService.createWorkType(workTypeDto2);
        //WHEN THEN
        mockMvc.perform(get("/api/work-types"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].workTypeName").value("Work Type 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mileageDuration").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].timeDuration").value(300))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].workTypeName").value("Work Type 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].mileageDuration").value(20000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].timeDuration").value(250));
    }
}