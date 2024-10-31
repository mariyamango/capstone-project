package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record WorkDto(String id, String carId, String type, int mileage, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate date, double price) {
}
