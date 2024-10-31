package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import com.example.backend.repository.WorkRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkService workService;

    public WorkServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllWorksByCarId_shouldReturnListOfWorkDtos() {
        // GIVEN
        String carId = "1";
        List<WorkDto> works = Arrays.asList(
                new WorkDto("1", carId, "Oil Change", 5000, LocalDate.of(2023,1,10), 50.0),
                new WorkDto("2", carId, "Tire Replacement", 8000, LocalDate.of(2023,2,15), 120.0)
        );
        when(workRepository.findAllByCarId(carId)).thenReturn(works);
        // WHEN
        List<WorkDto> result = workService.getAllWorksByCarId(carId);
        // THEN
        assertEquals(2, result.size());
        assertEquals("1", result.getFirst().id());
        assertEquals(carId, result.getFirst().carId());
        assertEquals("Oil Change", result.getFirst().type());
        assertEquals(5000, result.getFirst().mileage());
        assertEquals("2023-01-10", result.get(0).date().toString());
        assertEquals(50.0, result.get(0).price());
        assertEquals("2", result.get(1).id());
        assertEquals(carId, result.get(1).carId());
        assertEquals("Tire Replacement", result.get(1).type());
        assertEquals(8000, result.get(1).mileage());
        assertEquals("2023-02-15", result.get(1).date().toString());
        assertEquals(120.0, result.get(1).price());
        verify(workRepository, times(1)).findAllByCarId(carId);
    }

    @Test
    void getAllWorksByCarId_shouldReturnEmptyListWhenNoWorksFound() {
        // GIVEN
        String carId = "2";
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
        WorkDto workDto = new WorkDto("generated-id","generated-carId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        Work work = new Work("generated-id","generated-carId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        when(workRepository.save(any(Work.class))).thenReturn(work);
        //WHEN
        WorkDto result = workService.createWork(workDto);
        //THEN
        assertEquals("generated-carId", result.carId());
        assertEquals("Tires change", result.type());
        assertEquals(10000, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(50.0, result.price());
        verify(workRepository, times(1)).save(any(Work.class));
    }

    @Test
    void updateWork_shouldUpdateAndReturnUpdatedWork_whenWorkExists() {
        //GIVEN
        Work existingWork = new Work("generated-id","generated-carId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        WorkDto updatedWorkDto = new WorkDto("generated-id","generated-carId","Oil change",10500,LocalDate.of(2023,1,10),25.0);
        Work updatedWork = new Work("generated-id","generated-carId","Oil change",10500,LocalDate.of(2023,1,10),25.0);
        when(workRepository.findById("generated-id")).thenReturn(Optional.of(existingWork));
        when(workRepository.save(any(Work.class))).thenReturn(updatedWork);
        //WHEN
        WorkDto result = workService.updateWork("generated-id", updatedWorkDto);
        //THEN
        assertEquals("generated-carId", result.carId());
        assertEquals("Oil change", result.type());
        assertEquals(10500, result.mileage());
        assertEquals(LocalDate.of(2023,1,10), result.date());
        assertEquals(25.0, result.price());
        verify(workRepository, times(1)).findById("generated-id");
        verify(workRepository, times(1)).save(any(Work.class));
    }

    @Test
    void updateWork_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        WorkDto updatedWorkDto = new WorkDto("generated-id","generated-carId","Tires change",10000,LocalDate.of(2023,1,10),50.0);
        when(workRepository.findById("generated-id")).thenReturn(Optional.empty());
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.updateWork("generated-id", updatedWorkDto));
        verify(workRepository, times(1)).findById("generated-id");
        verify(workRepository, never()).save(any(Work.class));
    }

    @Test
    void deleteWorkById_shouldDeleteWork_whenWorkExists() {
        // GIVEN
        Work existingWork = new Work("generated-id", "generated-carId", "Tires change", 10000, LocalDate.of(2023, 1, 10), 50.0);
        when(workRepository.findById("generated-id")).thenReturn(Optional.of(existingWork));

        // WHEN
        workService.deleteWorkById("generated-id");

        // THEN
        verify(workRepository, times(1)).findById("generated-id");
        verify(workRepository, times(1)).delete(existingWork);
    }


    @Test
    void deleteWorkById_shouldThrowException_whenWorkNotFound() {
        //GIVEN
        when(workRepository.findById("not-existing-id")).thenReturn(Optional.empty());
        //WHEN THEN
        assertThrows(NoSuchElementException.class, () -> workService.deleteWorkById("not-existing-id"));
        verify(workRepository, times(1)).findById("not-existing-id");
        verify(workRepository, never()).delete(any(Work.class));
    }
}
