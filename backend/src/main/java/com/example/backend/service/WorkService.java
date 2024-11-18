package com.example.backend.service;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import com.example.backend.repository.WorkRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;

    public List<WorkDto> getAllWorksByCarId (String carId) {
        return workRepository.findAllByCarId(carId);
    }

    public WorkDto createWork(WorkDto workDto) {
        Work work = convertToEntity(workDto, workDto.id());
        workRepository.save(work);
        log.info("Created work with id: {}", work.id());
        return workDto;
    }

    public WorkDto updateWork(String id, WorkDto workDto) {
        workRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Work with id " + id + " not found."));
        Work newWork = convertToEntity(workDto, id);
        workRepository.save(newWork);
        log.info("Updated work with id: {}", id);
        return convertToDto(newWork);
    }

    public void deleteWorkById(String id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Work with id " + id + " not found."));
        workRepository.delete(work);
        log.info("Deleted work with id: {}", id);
    }

    private WorkDto convertToDto(Work work) {
        return new WorkDto(
                work.id(),
                work.carId(),
                work.workTypeId(),
                work.type(),
                work.mileage(),
                work.date(),
                work.price()
        );
    }

    private Work convertToEntity(WorkDto workDto, String id) {
        return new Work(
                id,
                workDto.carId(),
                workDto.workTypeId(),
                workDto.type(),
                workDto.mileage(),
                workDto.date(),
                workDto.price()
        );
    }
}
