package org.example.controller;

import org.example.Dto.ItemsManufacturers;
import org.example.domain.Item;
import org.example.domain.Manufacturer;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private DataService dataService; // Assuming you have DataService to fetch data from sources

    @GetMapping("/springmvc/data")
    public List<ItemsManufacturers> getData() {
        List<Item> items = dataService.fetchAllItems();
        List<Manufacturer> manufacturers = dataService.fetchAllManufacturers();

        return dataService.mergeLists(items, manufacturers);
    }
}
