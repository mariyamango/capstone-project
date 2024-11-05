package com.example.backend.dto;

public record CountdownResultDto(
        int mileageLeft,
        long daysLeft
) {}