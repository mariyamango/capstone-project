package com.example.backend.controller;

import com.example.backend.dto.CarDto;
import com.example.backend.dto.CreateCarRequest;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarHealthController {
    private final CarHealthService carHealthService;
    private final IdGeneratorService idGeneratorService;

    @GetMapping
    public List<CarDto> cars() {
        return carHealthService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarDto car(@PathVariable String id) {
        return carHealthService.getCarById(id);
    }

    @PostMapping
    public CarDto createCar(@RequestBody CreateCarRequest createCarRequest) {
        return carHealthService.createCar(new CarDto(idGeneratorService.generateId(),createCarRequest.model(),createCarRequest.year(),createCarRequest.vin()));
    }

    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable String id, @RequestBody CarDto carDto) {
        return carHealthService.updateCar(id, carDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        carHealthService.deleteCarById(id);
        return ResponseEntity.noContent().build();
    }
}
