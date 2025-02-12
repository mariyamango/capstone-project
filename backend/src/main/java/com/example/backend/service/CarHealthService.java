package com.example.backend.service;

import com.example.backend.dto.CarDto;
import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CarHealthService {
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized to view works for this car";
    private final CarHealthRepository carHealthRepository;

    public List<CarDto> getAllCars(String username) {
        return carHealthRepository.findAllByUserId(username).stream()
                .map(this::convertToDto)
                .toList();
    }

    public CarDto getCarById(String id) {
        Car car = carHealthRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(notFoundErrorText(id)));
        return convertToDto(car);
    }

    public CarDto createCar(CarDto carDto) {
        validateCarVinUniqueness(carDto.userId(), carDto.vin(), null);
        Car car = convertToEntity(carDto, carDto.id());
        carHealthRepository.save(car);
        log.info("Created Car with id: {}", carDto.id());
        return carDto;
    }

    public CarDto updateCar(String id, CarDto carDto) {
        carHealthRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(notFoundErrorText(id)));
        validateCarVinUniqueness(carDto.userId(), carDto.vin(), id);
        Car updatedCar = convertToEntity(carDto, id);
        carHealthRepository.save(updatedCar);
        log.info("Updated Car with id: {}", id);
        return carDto;
    }

    public void deleteCarById(String id) {
        carHealthRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(notFoundErrorText(id)));
        carHealthRepository.deleteById(id);
        log.info("Deleted Car with id: {}", id);
    }

    public String getOwner(String carId) {
        Car car = carHealthRepository.findById(carId)
                .orElseThrow(() -> new NoSuchElementException(notFoundErrorText(carId)));
        return car.userId();
    }

    private CarDto convertToDto(Car car) {
        return new CarDto(car.id(), car.userId(), car.model(), car.year(), car.vin(), car.currentMileage());
    }

    private Car convertToEntity(CarDto carDto, String id) {
        return new Car(id, carDto.userId(), carDto.model(), carDto.year(), carDto.vin(), carDto.currentMileage());
    }

    private void validateCarVinUniqueness(String userId, String vin, String currentCarId) {
        boolean isExisted = carHealthRepository.findAllByUserId(userId).stream()
                .anyMatch(car -> car.vin().equals(vin) && !Objects.equals(car.id(), currentCarId));
        if (isExisted) {
            throw new IllegalArgumentException("A car with this VIN already exists for the user.");
        }
    }

    private String notFoundErrorText (String id) {
        return "Car with id " + id + " not found";
    }

    public void checkOwner(String carId, String name) {
        String owner = getOwner(carId);
        if (!owner.equals(name)) {
            throw new AccessDeniedException(UNAUTHORIZED_ERROR_MESSAGE);
        }
    }
}
