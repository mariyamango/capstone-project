package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record Work(
        String id,
        String carId,
        String workTypeId,
        String type,
        int mileage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        double price
) {}