package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CarHealthController {
    private final CarHealthService carHealthService;
    private final IdGeneratorService idGeneratorService;
    private final WorkService workService;
    private final WorkTypeService workTypeService;
    private final CountdownCalculationService countdownCalculationService;

    @GetMapping("/cars")
    public List<CarDto> cars() {
        return carHealthService.getAllCars();
    }

    @GetMapping("/cars/{id}")
    public CarDto car(@PathVariable String id) {
        return carHealthService.getCarById(id);
    }

    @PostMapping("/cars")
    public CarDto createCar(@RequestBody CreateCarRequest createCarRequest) {
        return carHealthService.createCar(new CarDto(idGeneratorService.generateId(),createCarRequest.model(),createCarRequest.year(),createCarRequest.vin(), createCarRequest.currentMileage()));
    }

    @PutMapping("/cars/{id}")
    public CarDto updateCar(@PathVariable String id, @RequestBody CarDto carDto) {
        return carHealthService.updateCar(id, carDto);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        carHealthService.deleteCarById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/works/{carId}")
    public List<WorkDto> work(@PathVariable String carId) {
        return workService.getAllWorksByCarId(carId);
    }

    @PostMapping("/works")
    public WorkDto addWork(@RequestBody CreateWorkRequest createWorkRequest) {
        return workService.createWork(new WorkDto(idGeneratorService.generateId(), createWorkRequest.carId(), createWorkRequest.workTypeId(), createWorkRequest.type(), createWorkRequest.mileage(), createWorkRequest.date(), createWorkRequest.price()));
    }

    @PutMapping("/works/{id}")
    public WorkDto updateWork(@PathVariable String id, @RequestBody WorkDto workDto) {
        return workService.updateWork(id, workDto);
    }

    @DeleteMapping("/works/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable String id) {
        workService.deleteWorkById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/work-types")
    public List<WorkTypeDto> workTypes() {
        return workTypeService.getAllWorkTypes();
    }

    @GetMapping("/works/{carId}/countdowns")
    public List<WorkCountdownDto> getWorkCountdownsByCarId(@PathVariable String carId) {
        int currentMileage = carHealthService.getCarById(carId).currentMileage();
        List<WorkDto> works = workService.getAllWorksByCarId(carId);

        return works.stream()
                .map(work -> {
                    WorkTypeDto workType = workTypeService.getWorkTypeById(work.workTypeId());
                    CountdownResultDto result = countdownCalculationService.calculateCountdown(
                            workType.mileageDuration(),
                            workType.timeDuration(),
                            currentMileage,
                            work.mileage(),
                            work.date()
                    );
                    return new WorkCountdownDto(
                            work.id(),
                            work.carId(),
                            work.workTypeId(),
                            work.type(),
                            work.mileage(),
                            work.date(),
                            work.price(),
                            result.mileageLeft(),
                            result.daysLeft()
                    );
                }).toList();
    }
}
