package com.learnreactivespring.repository;

import com.learnreactivespring.document.Manufacturer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ManufacturerReactiveRepository extends ReactiveMongoRepository<Manufacturer,String> {

}