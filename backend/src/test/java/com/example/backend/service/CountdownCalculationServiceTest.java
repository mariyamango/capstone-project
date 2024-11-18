package com.example.backend.service;

import com.example.backend.dto.CountdownResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountdownCalculationServiceTest {

    private CountdownCalculationService countdownCalculationService;

    @BeforeEach
    void setUp() {
        countdownCalculationService = new CountdownCalculationService();
    }

    @Test
    void calculateCountdown_shouldReturnCorrectCountdown() {
        //GIVEN
        int mileageDuration = 10000;
        int timeDuration = 180;
        int currentMileage = 15000;
        int lastWorkMileage = 14000;
        LocalDate lastWorkDate = LocalDate.of(2023, 1, 10);
        //WHEN
        CountdownResultDto result = countdownCalculationService.calculateCountdown(mileageDuration, timeDuration, currentMileage, lastWorkMileage, lastWorkDate);
        //THEN
        assertEquals(10000 - (15000 - 14000), result.mileageLeft());
        assertEquals(180 - ChronoUnit.DAYS.between(LocalDate.of(2023, 1, 10), LocalDate.now()), result.daysLeft());
    }

    @Test
    void calculateCountdown_shouldHandleZeroMileageLeft() {
        //GIVEN
        int mileageDuration = 10000;
        int timeDuration = 180;
        int currentMileage = 20000;
        int lastWorkMileage = 10000;
        LocalDate lastWorkDate = LocalDate.of(2023, 1, 10);
        //WHEN
        CountdownResultDto result = countdownCalculationService.calculateCountdown(mileageDuration, timeDuration, currentMileage, lastWorkMileage, lastWorkDate);
        //THEN
        assertEquals(10000 - (20000 - 10000), result.mileageLeft());
        assertEquals(180 - ChronoUnit.DAYS.between(LocalDate.of(2023, 1, 10), LocalDate.now()), result.daysLeft());
    }

    @Test
    void calculateCountdown_shouldReturnNegativeDaysLeft() {
        //GIVEN
        int mileageDuration = 10000;
        int timeDuration = 60;
        int currentMileage = 15000;
        int lastWorkMileage = 14000;
        LocalDate lastWorkDate = LocalDate.of(2023, 1, 10);
        //WHEN
        CountdownResultDto result = countdownCalculationService.calculateCountdown(mileageDuration, timeDuration, currentMileage, lastWorkMileage, lastWorkDate);
        //THEN
        assertEquals(10000 - (15000 - 14000), result.mileageLeft());
        assertEquals(60 - ChronoUnit.DAYS.between(LocalDate.of(2023, 1, 10), LocalDate.now()), result.daysLeft());
    }

    @Test
    void calculateCountdown_shouldReturnZeroDaysLeftIfOnTheDay() {
        //GIVEN
        int mileageDuration = 10000;
        int timeDuration = 0;
        int currentMileage = 15000;
        int lastWorkMileage = 14000;
        LocalDate lastWorkDate = LocalDate.now();
        //WHEN
        CountdownResultDto result = countdownCalculationService.calculateCountdown(mileageDuration, timeDuration, currentMileage, lastWorkMileage, lastWorkDate);
        //THEN
        assertEquals(10000 - (15000 - 14000), result.mileageLeft());
        assertEquals(0, result.daysLeft());
    }

    @Test
    void calculateCountdown_shouldThrowException_whenMileageDurationIsNegative() {
        //GIVEN
        int mileageDuration = -10;
        int timeDuration = 10;
        int currentMileage = 15000;
        int lastWorkMileage = 14000;
        LocalDate lastWorkDate = LocalDate.now();
        //WHEN THEN
        assertThrows(IllegalArgumentException.class, () -> {countdownCalculationService.calculateCountdown(mileageDuration,timeDuration,currentMileage,lastWorkMileage,lastWorkDate);});
    }

    @Test
    void calculateCountdown_shouldThrowException_whenTimeDurationIsNegative() {
        //GIVEN
        int mileageDuration = 1000;
        int timeDuration = -10;
        int currentMileage = 15000;
        int lastWorkMileage = 14000;
        LocalDate lastWorkDate = LocalDate.now();
        //WHEN THEN
        assertThrows(IllegalArgumentException.class, () -> {countdownCalculationService.calculateCountdown(mileageDuration,timeDuration,currentMileage,lastWorkMileage,lastWorkDate);});
    }
}
