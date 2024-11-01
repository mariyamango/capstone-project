package com.example.backend.service;

import com.example.backend.dto.WorkTypeDto;
import com.example.backend.model.WorkType;
import com.example.backend.repository.WorkTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkTypeServiceTest {

    @Autowired
    private WorkTypeRepository workTypeRepository;

    @Autowired
    private WorkTypeService workTypeService;

    @BeforeEach
    void setUp() {
        workTypeRepository.deleteAll();
    }

    @Test
    void getAllWorkTypes_shouldReturnAllWorkTypesSortedAscending() {
        //GIVEN
        WorkType workType1 = new WorkType("1", "A work type",100,200);
        WorkType workType2 = new WorkType("2", "C work type",10,30);
        WorkType workType3 = new WorkType("3", "B work type",100,400);
        workTypeRepository.saveAll(List.of(workType1, workType2, workType3));
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
    }
}