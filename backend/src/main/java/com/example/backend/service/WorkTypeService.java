package com.example.backend.service;

import com.example.backend.dto.WorkTypeDto;
import com.example.backend.repository.WorkTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkTypeService {
    private final WorkTypeRepository workTypeRepository;

    public List<WorkTypeDto> getAllWorkTypes() {
        return workTypeRepository.findAllByOrderByWorkTypeNameAsc().stream()
                .map(workType -> new WorkTypeDto(workType.id(), workType.workTypeName(), workType.mileageDuration(), workType.timeDuration()))
                .toList();
    }
}
