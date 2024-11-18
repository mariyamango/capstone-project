package com.example.backend.service;

import com.example.backend.dto.CountdownResultDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CountdownCalculationService {

    private static final int MINIMUM_MILEAGE = 0;
    private static final int MINIMUM_DAYS = 0;

    public CountdownResultDto calculateCountdown(int mileageDuration, int timeDuration, int currentMileage, int lastWorkMileage, LocalDate lastWorkDate) {
        if (mileageDuration < MINIMUM_MILEAGE || timeDuration < MINIMUM_DAYS) {
            throw new IllegalArgumentException("Duration values must be positive.");
        }
        int mileageLeft = mileageDuration - (currentMileage - lastWorkMileage);
        long daysSinceLastWork = ChronoUnit.DAYS.between(lastWorkDate, LocalDate.now());
        long daysLeft = timeDuration - daysSinceLastWork;
        return new CountdownResultDto(mileageLeft, daysLeft);
    }
}