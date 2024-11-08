package com.example.backend.dto;

public record CreateCarRequest (
        String model,
        String userId,
        int year,
        String vin,
        int currentMileage
) {}