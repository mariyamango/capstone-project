package com.example.backend.repository;

import com.example.backend.dto.WorkDto;
import com.example.backend.model.Work;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends MongoRepository<Work,String> {

    List<WorkDto> findAllByCarId(String carId);
}
