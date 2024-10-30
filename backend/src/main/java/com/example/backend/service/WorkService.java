package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    public List<WorkDto> getAllWorksByCarId (String carId) {
        return workRepository.findAllByCarId(carId);
    }
}
