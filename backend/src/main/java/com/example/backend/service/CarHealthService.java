package com.example.backend.service;

import com.example.backend.dto.CarDto;
import com.example.backend.model.Car;
import com.example.backend.repository.CarHealthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CarHealthService {
    private final CarHealthRepository carHealthRepository;

    public List<CarDto> getAllCars(String username) {
        return carHealthRepository.findAllByUserId(username).stream()
                .map(car -> new CarDto(car.id(), car.userId(), car.model(), car.year(), car.vin(), car.currentMileage()))
                .toList();
    }

    public CarDto getCarById(String id) {
        Car car = carHealthRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return new CarDto(car.id(), car.userId(), car.model(), car.year(), car.vin(), car.currentMileage());
    }

    public CarDto createCar(CarDto carDto) {
        boolean isExisted = carHealthRepository.findAllByUserId(carDto.userId()).stream()
                .anyMatch(car -> car.vin().equals(carDto.vin()));
        if (isExisted) {
            throw new IllegalArgumentException("A car with this VIN already exists for the user.");
        }
        Car car = new Car(carDto.id(), carDto.userId(), carDto.model(), carDto.year(), carDto.vin(), carDto.currentMileage());
        carHealthRepository.save(car);
        return carDto;
    }

    public CarDto updateCar(String id, CarDto carDto) {
        Car existingCar = carHealthRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no car with this id."));
        boolean isExisted = carHealthRepository.findAllByUserId(carDto.userId()).stream()
                .anyMatch(car -> car.vin().equals(carDto.vin()) && !car.id().equals(id));
        if (isExisted) {
            throw new IllegalArgumentException("A car with this VIN already exists for the user.");
        }
        Car newCar = new Car(id, carDto.userId(), carDto.model(), carDto.year(), carDto.vin(), carDto.currentMileage());
        carHealthRepository.save(newCar);
        return carDto;
    }

    public void deleteCarById(String id) {
        carHealthRepository.findById(id).orElseThrow(NoSuchElementException::new);
        carHealthRepository.deleteById(id);
    }

    public String getOwner(String carId) {
        Car car = carHealthRepository.findById(carId).orElseThrow(NoSuchElementException::new);
        return car.userId();
    }
}
