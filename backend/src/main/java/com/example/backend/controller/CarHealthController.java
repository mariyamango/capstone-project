package com.example.backend.controller;

import com.example.backend.dto.CarDto;
import com.example.backend.dto.CreateCarRequest;
import com.example.backend.dto.WorkDto;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import com.example.backend.service.WorkService;
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
        return carHealthService.createCar(new CarDto(idGeneratorService.generateId(),createCarRequest.model(),createCarRequest.year(),createCarRequest.vin()));
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
}
