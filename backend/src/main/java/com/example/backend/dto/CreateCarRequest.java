package com.example.backend.dto;

public record CreateCarRequest (
        String model,
        int year,
        String vin,
        int currentMileage
) {}