package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.repository.WorkRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                new WorkDto("1", carId, "Oil Change", 5000, "2023-01-10", 50.0),
                new WorkDto("2", carId, "Tire Replacement", 8000, "2023-02-15", 120.0)
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
        assertEquals("2023-01-10", result.get(0).date());
        assertEquals(50.0, result.get(0).price());
        assertEquals("2", result.get(1).id());
        assertEquals(carId, result.get(1).carId());
        assertEquals("Tire Replacement", result.get(1).type());
        assertEquals(8000, result.get(1).mileage());
        assertEquals("2023-02-15", result.get(1).date());
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
}
