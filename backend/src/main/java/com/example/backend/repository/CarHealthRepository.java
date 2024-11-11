package com.example.backend.repository;

import com.example.backend.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarHealthRepository extends MongoRepository <Car, String> {

    List<Car> findAllByUserId(String userId);
}
