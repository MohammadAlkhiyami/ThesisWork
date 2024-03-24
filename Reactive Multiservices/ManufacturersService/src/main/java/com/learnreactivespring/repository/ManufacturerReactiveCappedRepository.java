package com.learnreactivespring.repository;

import com.learnreactivespring.document.ManufacturerCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ManufacturerReactiveCappedRepository extends ReactiveMongoRepository<ManufacturerCapped,String> {

    @Tailable
    Flux<ManufacturerCapped> findManufacturersBy();
}
