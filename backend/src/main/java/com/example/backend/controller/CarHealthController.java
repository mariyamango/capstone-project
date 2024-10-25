package com.example.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CarHealthController {

    @GetMapping("/cars")
    public String cars() {
        return "Car Health Controller";
    }
}
