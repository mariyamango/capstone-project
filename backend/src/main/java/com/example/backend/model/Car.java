package com.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public record Car(@Id String id, String model, int year, String vin, int currentMileage) {
}