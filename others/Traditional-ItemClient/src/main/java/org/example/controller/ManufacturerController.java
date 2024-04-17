package org.example.controller;

import org.example.domain.Item;
import org.example.domain.Manufacturer;
import org.example.service.ItemService;
import org.example.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    ItemService itemService;

    @PostMapping("/addManufacturer")
    public String saveManufacturer(@RequestBody Manufacturer manufacturer){
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @PostMapping("/addManufacturers")
    public String saveManufacturers(@RequestBody ArrayList<Manufacturer> manufacturers){
        return manufacturerService.saveManufacturers(manufacturers);
    }

    @GetMapping("/findAllManufacturers")
    public List<Manufacturer> getManufacturers() {

        return manufacturerService.getManufacturers();
    }

    @GetMapping("/getManufacturer/{id}")
    public Optional<Manufacturer> getManufacturer(@PathVariable int id) {

        if(manufacturerService.getManufacturer(id).isPresent()){
            return manufacturerService.getManufacturer(id);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable int id){

        return manufacturerService.deleteManufacturer(id);
    }
}
