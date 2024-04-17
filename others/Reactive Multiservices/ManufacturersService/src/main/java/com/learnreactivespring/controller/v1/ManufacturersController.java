package com.learnreactivespring.controller.v1;

import com.learnreactivespring.document.Manufacturer;
import com.learnreactivespring.repository.ManufacturerReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.learnreactivespring.constants.ManufacturersConstants.*;

@RestController
@Slf4j
public class ManufacturersController {


    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    @GetMapping(MANUFACTURERS_END_POINT_V1)
    public Flux<Manufacturer> getAllManufacturers() {

        return manufacturerReactiveRepository.findAll();

    }

    @GetMapping(MANUFACTURERS_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Manufacturer>> getOneManufacturer(@PathVariable String id) {

        return manufacturerReactiveRepository.findById(id)
                .map((manufacturer) -> new ResponseEntity<>(manufacturer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @PostMapping(MANUFACTURERS_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {

        return manufacturerReactiveRepository.save(manufacturer);


    }

    @DeleteMapping(MANUFACTURERS_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteManufacturer(@PathVariable String id) {

        return manufacturerReactiveRepository.deleteById(id);

    }

    @GetMapping(MANUFACTURERS_END_POINT_V1 + "/runtimeException")
    public Flux<Manufacturer> runtimeException() {

        return manufacturerReactiveRepository.findAll()
                .concatWith(Mono.error(new RuntimeException("RuntimeException Occurred.")));
    }

    @PutMapping(MANUFACTURERS_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Manufacturer>> updateManufacturer(@PathVariable String id,
                                                 @RequestBody Manufacturer manufacturer) {

        return manufacturerReactiveRepository.findById(id)
                .flatMap(currentManufacturer -> {

                    currentManufacturer.setName(manufacturer.getName());
                    currentManufacturer.setAddress(manufacturer.getAddress());
                    return manufacturerReactiveRepository.save(currentManufacturer);
                })
                .map(updatedManufacturer -> new ResponseEntity<>(updatedManufacturer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


}
