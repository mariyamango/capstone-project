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

    public List<CarDto> getAllCars() {
        return carHealthRepository.findAll().stream()
                .map(car -> new CarDto(car.id(), car.model(), car.year(), car.vin()))
                .toList();
    }

    public CarDto getCarById(String id) {
        Car car = carHealthRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return new CarDto(car.id(), car.model(), car.year(), car.vin());
    }

    public CarDto createCar(CarDto carDto) {
        Car car = new Car(carDto.id(), carDto.model(), carDto.year(), carDto.vin());
        carHealthRepository.save(car);
        return carDto;
    }

    public CarDto updateCar(String id, CarDto carDto) {
        Car car = carHealthRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Car newCar = new Car(car.id(), carDto.model(), carDto.year(), carDto.vin());
        carHealthRepository.save(newCar);
        return carDto;
    }

    public void deleteCarById(String id) {
        carHealthRepository.findById(id).orElseThrow(NoSuchElementException::new);
        carHealthRepository.deleteById(id);
    }
}
