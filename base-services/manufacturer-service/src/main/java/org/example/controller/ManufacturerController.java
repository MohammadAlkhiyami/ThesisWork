package org.example.controller;

import org.example.domain.Manufacturer;
import org.example.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;

    @PostMapping("/manufacturers")
    public String saveManufacturer(@RequestBody Manufacturer manufacturer){
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @GetMapping("/manufacturers")
    public List<Manufacturer> getManufacturers() {
        return manufacturerService.getManufacturers();
    }

    @GetMapping("/manufacturers/{id}")
    public Optional<Manufacturer> getManufacturer(@PathVariable int id) {
        if(manufacturerService.getManufacturer(id).isPresent()){
            return manufacturerService.getManufacturer(id);
        }
        return null;
    }

    @DeleteMapping("/manufacturers/{id}")
    public String deleteManufacturer(@PathVariable int id){
        return manufacturerService.deleteManufacturer(id);
    }
}
