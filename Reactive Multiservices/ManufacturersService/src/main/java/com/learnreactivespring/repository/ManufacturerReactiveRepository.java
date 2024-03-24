package com.learnreactivespring.repository;

import com.learnreactivespring.document.Manufacturer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ManufacturerReactiveRepository extends ReactiveMongoRepository<Manufacturer,String> {
    Mono<Manufacturer> findByName(String description);

}