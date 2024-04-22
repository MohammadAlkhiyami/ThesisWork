package com.learnreactivespring.controller.v1;

import com.learnreactivespring.Dto.ItemsManufacturers;
import com.learnreactivespring.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
public class DataController {

    @Autowired
    private DataService dataService; // Assuming you have DataService to fetch data from sources

    @GetMapping("/springwebflux/data")
    public Mono<List<ItemsManufacturers>> getData() {
        return dataService.getData();
    }

}
