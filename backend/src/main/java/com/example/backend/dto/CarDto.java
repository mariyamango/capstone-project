package com.example.backend.dto;

public record CarDto(
        String id,
        String userId,
        String model,
        int year,
        String vin,
        int currentMileage
) {}