package com.learnreactivespring.controller.v1;


import com.learnreactivespring.document.ManufacturerCapped;
import com.learnreactivespring.repository.ManufacturerReactiveCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.learnreactivespring.constants.ManufacturersConstants.*;

@RestController
public class ManufacturerStreamController {

    @Autowired
    ManufacturerReactiveCappedRepository manufacturerReactiveCappedRepository;

    @GetMapping(value = MANUFACTURERS_END_POINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ManufacturerCapped> getManufacturersStream(){

        return manufacturerReactiveCappedRepository.findManufacturersBy();
    }




}
