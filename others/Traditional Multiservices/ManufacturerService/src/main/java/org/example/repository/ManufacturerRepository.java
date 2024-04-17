package org.example.repository;

import org.example.domain.Manufacturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends MongoRepository<Manufacturer, Integer> {
}
