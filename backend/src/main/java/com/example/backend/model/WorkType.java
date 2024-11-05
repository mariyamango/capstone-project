package com.example.backend.model;

public record WorkType(
        String id,
        String workTypeName,
        int mileageDuration,
        int timeDuration
) {}