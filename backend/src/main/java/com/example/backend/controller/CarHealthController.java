package com.example.backend.controller;

import com.example.backend.dto.CarDto;
import com.example.backend.dto.CreateCarRequest;
import com.example.backend.service.CarHealthService;
import com.example.backend.service.IdGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CarHealthController {
    private final CarHealthService carHealthService;
    private final IdGeneratorService idGeneratorService;

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

    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable String id) {
        carHealthService.deleteCarById(id);
    }
}
