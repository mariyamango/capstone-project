package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<CarDto> cars(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        return carHealthService.getAllCars(username);
    }

    @GetMapping("/cars/{id}")
    public CarDto car(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable String id) {
        checkOwner(oAuth2User, id);
        return carHealthService.getCarById(id);
    }

    @PostMapping("/cars")
    public CarDto createCar(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody CreateCarRequest createCarRequest) {
        String userId = oAuth2User.getName();
        return carHealthService.createCar(new CarDto(idGeneratorService.generateId(), userId, createCarRequest.model(),createCarRequest.year(),createCarRequest.vin(), createCarRequest.currentMileage()));
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
    public List<WorkDto> work(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable String carId) {
        checkOwner(oAuth2User, carId);
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
    public WorkSummaryResponseDto getWorkCountdownsByCarId(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable String carId) {
        checkOwner(oAuth2User, carId);
        int currentMileage = carHealthService.getCarById(carId).currentMileage();
        List<WorkDto> works = workService.getAllWorksByCarId(carId);
        Map<String, BigDecimal> totalByType = new HashMap<>();
        BigDecimal[] grandTotal = { BigDecimal.ZERO };

        List<WorkCountdownDto> workCountdowns = works.stream()
                .map(work -> {
                    WorkTypeDto workType = workTypeService.getWorkTypeById(work.workTypeId());
                    CountdownResultDto result = countdownCalculationService.calculateCountdown(
                            workType.mileageDuration(),
                            workType.timeDuration(),
                            currentMileage,
                            work.mileage(),
                            work.date()
                    );
                    BigDecimal currentTotal = totalByType.getOrDefault(work.type(), BigDecimal.ZERO);
                    totalByType.put(work.type(), currentTotal.add(BigDecimal.valueOf(work.price())));
                    grandTotal[0] = grandTotal[0].add(BigDecimal.valueOf(work.price()));
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
        return new WorkSummaryResponseDto(workCountdowns,totalByType,grandTotal[0]);
    }

    private void checkOwner(OAuth2User oAuth2User, String carId) {
        carHealthService.checkOwner(carId, oAuth2User.getName());
    }
}
