package com.example.backend.service;

import com.example.backend.dto.WorkTypeDto;
import com.example.backend.model.WorkType;
import com.example.backend.repository.WorkTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkTypeServiceTest {

    @Mock
    private WorkTypeRepository workTypeRepository;

    @InjectMocks
    private WorkTypeService workTypeService;

    @Test
    void getAllWorkTypes_shouldReturnAllWorkTypesSortedAscending() {
        //GIVEN
        WorkType workType1 = new WorkType("1", "A work type",100,200);
        WorkType workType2 = new WorkType("2", "C work type",10,30);
        WorkType workType3 = new WorkType("3", "B work type",100,400);
        when(workTypeRepository.findAllByOrderByWorkTypeNameAsc()).thenReturn(List.of(workType1, workType3, workType2));
        //WHEN
        List<WorkTypeDto> result = workTypeService.getAllWorkTypes();
        //THEN
        assertEquals(3, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("A work type",result.get(0).workTypeName());
        assertEquals("3", result.get(1).id());
        assertEquals("B work type",result.get(1).workTypeName());
        assertEquals("2", result.get(2).id());
        assertEquals("C work type",result.get(2).workTypeName());
        verify(workTypeRepository, times(1)).findAllByOrderByWorkTypeNameAsc();
    }

    @Test
    void createWorkType_shouldCreateNewWorkType() {
        //GIVEN
        WorkType workType = new WorkType("1", "A work type",100,200);
        WorkTypeDto workTypeDto = new WorkTypeDto("1", "A work type",100,200);
        when(workTypeRepository.save(workType)).thenReturn(workType);
        //WHEN
        WorkTypeDto result = workTypeService.createWorkType(workTypeDto);
        //THEN
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("A work type",result.workTypeName());
        assertEquals(100, result.mileageDuration());
        assertEquals(200, result.timeDuration());
        verify(workTypeRepository, times(1)).save(workType);
    }
}