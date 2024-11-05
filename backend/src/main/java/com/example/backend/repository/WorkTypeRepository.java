package com.example.backend.repository;

import com.example.backend.model.WorkType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTypeRepository extends MongoRepository<WorkType, String> {
    List<WorkType> findAllByOrderByWorkTypeNameAsc();
}
