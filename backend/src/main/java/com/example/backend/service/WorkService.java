package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import com.example.backend.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    public List<WorkDto> getAllWorksByCarId (String carId) {
        return workRepository.findAllByCarId(carId);
    }

    public WorkDto createWork(WorkDto workDto) {
        Work work = new Work(workDto.id(), workDto.carId(), workDto.workTypeId(), workDto.type(), workDto.mileage(), workDto.date(), workDto.price());
        workRepository.save(work);
        return workDto;
    }

    public WorkDto updateWork(String id, WorkDto workDto) {
        Work work = workRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Work newWork = new Work(id, workDto.carId(), workDto.workTypeId(), workDto.type(), workDto.mileage(), workDto.date(), workDto.price());
        workRepository.save(newWork);
        return new WorkDto(newWork.id(), newWork.carId(), workDto.workTypeId(), newWork.type(), newWork.mileage(), newWork.date(), newWork.price());
    }

    public void deleteWorkById(String id) {
        Work work = workRepository.findById(id).orElseThrow(NoSuchElementException::new);
        workRepository.delete(work);
    }
}
