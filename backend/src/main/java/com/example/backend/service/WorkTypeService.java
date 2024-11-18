package com.example.backend.service;

import com.example.backend.dto.WorkTypeDto;
import com.example.backend.model.WorkType;
import com.example.backend.repository.WorkTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WorkTypeService {
    private final WorkTypeRepository workTypeRepository;

    public List<WorkTypeDto> getAllWorkTypes() {
        return workTypeRepository.findAllByOrderByWorkTypeNameAsc().stream()
                .map(this::convertToDto)
                .toList();
    }

    public WorkTypeDto createWorkType(WorkTypeDto workTypeDto) {
        workTypeRepository.save(convertToEntity(workTypeDto, workTypeDto.id()));
        return workTypeDto;
    }

    public WorkTypeDto getWorkTypeById(String id) {
        return workTypeRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("WorkType with ID " + id + " not found"));
    }

    private WorkTypeDto convertToDto(WorkType workType) {
        return new WorkTypeDto(
                workType.id(),
                workType.workTypeName(),
                workType.mileageDuration(),
                workType.timeDuration()
        );
    }

    private WorkType convertToEntity(WorkTypeDto workTypeDto, String id) {
        return new WorkType(
                id,
                workTypeDto.workTypeName(),
                workTypeDto.mileageDuration(),
                workTypeDto.timeDuration()
        );
    }
}
