package com.example.backend.service;

import com.example.backend.dto.CountdownResultDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CountdownCalculationService {

    public CountdownResultDto calculateCountdown(int mileageDuration, int timeDuration, int currentMileage, int lastWorkMileage, LocalDate lastWorkDate) {
        int mileageLeft = mileageDuration - (currentMileage - lastWorkMileage);
        long daysSinceLastWork = ChronoUnit.DAYS.between(lastWorkDate, LocalDate.now());
        long daysLeft = timeDuration - daysSinceLastWork;
        return new CountdownResultDto(mileageLeft, daysLeft);
    }
}