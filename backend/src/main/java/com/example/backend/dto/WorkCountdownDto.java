package com.example.backend.dto;

import java.time.LocalDate;

public record WorkCountdownDto(
        String id,
        String carId,
        String workTypeId,
        String type,
        int mileage,
        LocalDate date,
        double price,
        int mileageLeft,
        long daysLeft
) {}