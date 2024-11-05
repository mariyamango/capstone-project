package com.example.backend.dto;

public record CarDto(
        String id,
        String model,
        int year,
        String vin,
        int currentMileage
) {}