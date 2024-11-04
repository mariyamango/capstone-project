package com.example.backend.service;

import com.example.backend.dto.WorkTypeDto;
import com.example.backend.model.WorkType;
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

    public WorkTypeDto createWorkType(WorkTypeDto workTypeDto) {
        workTypeRepository.save(new WorkType(workTypeDto.id(), workTypeDto.workTypeName(), workTypeDto.mileageDuration(), workTypeDto.timeDuration()));
        return workTypeDto;
    }
}
