package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import com.example.backend.repository.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkServiceTest {

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private WorkService workService;

    @BeforeEach
    void setUp() {
        workRepository.deleteAll();
    }

    @Test
    void getAllWorksByCarId_shouldReturnListOfWorkDtos() {
        //GIVEN
        String carId = "123";
        Work work1 = new Work("1", carId, "workTypeId","Oil Change", 5000, LocalDate.of(2023,1,10), 50.0);
        Work work2 = new Work("2", carId, "workTypeId","Tire Replacement", 8000, LocalDate.of(2023,2,15), 120.0);
        workRepository.saveAll(Arrays.asList(work1, work2));
        //WHEN
        List<WorkDto> result = workService.getAllWorksByCarId(carId);
        //THEN
        assertEquals(2, result.size());
        assertEquals("1", result.getFirst().id());
        assertEquals("123", result.getFirst().carId());
        assertEquals("Oil Change", result.getFirst().type());
        assertEquals(5000, result.getFirst().mileage());
        assertEquals("2023-01-10", result.getFirst().date().toString());
        assertEquals(50.0, result.getFirst().price());
        assertEquals("2", result.get(1).id());
        assertEquals("123", result.get(1).carId());
        assertEquals("Tire Replacement", result.get(1).type());
        assertEquals(8000, result.get(1).mileage());
        assertEquals("2023-02-15", result.get(1).date().toString());
        assertEquals(120.0, result.get(1).price());
    }

    @Test
    void getAllWorksByCarId_shouldReturnEmptyListWhenNoWorksFound() {
        // GIVEN
        String carId = "2";
        // WHEN
        List<WorkDto> result = workService.getAllWorksByCarId(carId);
        // THEN
        assertEquals(0, result.size());
    }

    @Test
    void createWork_shouldSaveAndReturnWork() {
        //GIVEN
        WorkDto workDto = new WorkDto("generated-id","generated-carId","workTypeId", "Tires change",10000,LocalDate.of(2023,1,10),50.0);
        //WHEN
        WorkDto result = workService.createWork(workDto);
        //THEN
        assertEquals("generated-id", result.id());
        assertEquals("generated-carId", result.carId());
        assertEquals("workTypeId", result.workTypeId());
        assertEquals("Tires change", result.type());
        assertEquals(10000, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(50.0, result.price());
    }

    @Test
    void updateWork_shouldUpdateAndReturnUpdatedWork_whenWorkExists() {
        //GIVEN
        String id = "generated-id";
        Work existingWork = new Work(id,"generated-carId","workTypeId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        workRepository.save(existingWork);
        WorkDto updatedWorkDto = new WorkDto(id,"generated-carId","workTypeId","Oil change",10500,LocalDate.of(2023,1,10),25.0);
        //WHEN
        WorkDto result = workService.updateWork(id, updatedWorkDto);
        //THEN
        assertEquals("generated-carId", result.carId());
        assertEquals("Oil change", result.type());
        assertEquals(10500, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(25.0, result.price());
    }

    @Test
    void updateWork_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        String id = "generated-id";
        WorkDto updatedWorkDto = new WorkDto(id,"generated-carId","workTypeId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.updateWork(id, updatedWorkDto));
    }

    @Test
    void deleteWorkById_shouldDeleteWork_whenWorkExists() {
        // GIVEN
        String id = "generated-id";
        Work existingWork = new Work(id, "generated-carId", "workTypeId","Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        workRepository.save(existingWork);
        // WHEN
        workService.deleteWorkById(id);
        Optional<Work> deletedWork = workRepository.findById(id);
        // THEN
        assertTrue(deletedWork.isEmpty());
    }


    @Test
    void deleteWorkById_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        String notExistingId = "generated-not-existing-id";
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.deleteWorkById(notExistingId));
    }
}
