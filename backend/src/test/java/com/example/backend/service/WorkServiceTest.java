package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import com.example.backend.repository.WorkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkService workService;

    @Test
    void getAllWorksByCarId_shouldReturnListOfWorkDtos() {
        //GIVEN
        String carId = "123";
        WorkDto workDto1 = new WorkDto("1", carId, "workTypeId","Oil Change", 5000, LocalDate.of(2023,1,10), 50.0);
        WorkDto workDto2 = new WorkDto("2", carId, "workTypeId","Tire Replacement", 8000, LocalDate.of(2023,2,15), 120.0);
        when(workRepository.findAllByCarId(carId)).thenReturn(Arrays.asList(workDto1, workDto2));
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
        verify(workRepository, times(1)).findAllByCarId(carId);
    }

    @Test
    void getAllWorksByCarId_shouldReturnEmptyListWhenNoWorksFound() {
        // GIVEN
        String carId = "123";
        when(workRepository.findAllByCarId(carId)).thenReturn(Collections.emptyList());
        // WHEN
        List<WorkDto> result = workService.getAllWorksByCarId(carId);
        // THEN
        assertEquals(0, result.size());
        verify(workRepository, times(1)).findAllByCarId(carId);
    }

    @Test
    void createWork_shouldSaveAndReturnWork() {
        //GIVEN
        Work work = new Work("generated-id","generated-carId","workTypeId", "Tires change",10000,LocalDate.of(2023,1,10),50.0);
        WorkDto workDto = new WorkDto("generated-id","generated-carId","workTypeId", "Tires change",10000,LocalDate.of(2023,1,10),50.0);
        when(workRepository.save(work)).thenReturn(work);
        //WHEN
        WorkDto result = workService.createWork(workDto);
        //THEN
        assertEquals("generated-carId", result.carId());
        assertEquals("workTypeId", result.workTypeId());
        assertEquals("Tires change", result.type());
        assertEquals(10000, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(50.0, result.price());
        verify(workRepository, times(1)).save(work);
    }

    @Test
    void updateWork_shouldUpdateAndReturnUpdatedWork_whenWorkExists() {
        //GIVEN
        String id = "generated-id";
        Work existingWork = new Work(id,"generated-carId","workTypeId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        when(workRepository.findById(id)).thenReturn(Optional.of(existingWork));
        Work updatedWork = new Work(id,"generated-carId","workTypeId","Oil change",10500,LocalDate.of(2023,1,10),25.0);
        WorkDto updatedWorkDto = new WorkDto(id,"generated-carId","workTypeId","Oil change",10500,LocalDate.of(2023,1,10),25.0);
        //WHEN
        WorkDto result = workService.updateWork(id, updatedWorkDto);
        //THEN
        assertEquals("generated-carId", result.carId());
        assertEquals("workTypeId", result.workTypeId());
        assertEquals("Oil change", result.type());
        assertEquals(10500, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(25.0, result.price());
        verify(workRepository, times(1)).findById(id);
        verify(workRepository, times(1)).save(updatedWork);
    }

    @Test
    void updateWork_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        String id = "generated-id";
        when(workRepository.findById(id)).thenReturn(Optional.empty());
        WorkDto updatedWorkDto = new WorkDto(id,"generated-carId","workTypeId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.updateWork(id, updatedWorkDto));
        verify(workRepository, times(1)).findById(id);
        verify(workRepository, times(0)).save(any(Work.class));
    }

    @Test
    void deleteWorkById_shouldDeleteWork_whenWorkExists() {
        // GIVEN
        String id = "generated-id";
        Work existingWork = new Work(id,"generated-carId","workTypeId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        when(workRepository.findById(id)).thenReturn(Optional.of(existingWork));
        // WHEN
        workService.deleteWorkById(id);
        // THEN
        verify(workRepository, times(1)).findById(id);
        verify(workRepository, times(1)).delete(existingWork);
    }


    @Test
    void deleteWorkById_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        String notExistingId = "generated-not-existing-id";
        when(workRepository.findById(notExistingId)).thenReturn(Optional.empty());
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.deleteWorkById(notExistingId));
        verify(workRepository, times(1)).findById(notExistingId);
        verify(workRepository, times(0)).delete(any(Work.class));
    }
}