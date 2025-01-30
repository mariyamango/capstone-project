package com.example.backend.service;

import com.example.backend.dto.JokeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChucksJokeService {
    private final RestTemplate restTemplate;
    private static final String JOKE_API_URL = "https://geek-jokes.sameerkumar.website/api?format=json";

    public ChucksJokeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getJoke() {
        try {
            JokeResponse response = restTemplate.getForObject(JOKE_API_URL, JokeResponse.class);
            return response != null ? response.joke() : "No joke found.";
        } catch (Exception e) {
            return "Failed to retrieve a joke.";
        }
    }
}