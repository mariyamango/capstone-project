package com.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGeneratorService {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
