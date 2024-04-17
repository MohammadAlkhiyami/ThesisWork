package org.example.service;

import org.example.domain.Manufacturer;
import org.example.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    @Autowired
    ManufacturerRepository manufacturerRepository;

    public List<Manufacturer> getManufacturers(){
        return manufacturerRepository.findAll();
    }

    public Optional<Manufacturer> getManufacturer(Integer id){
        return manufacturerRepository.findById(id);
    }

    public String saveManufacturer(Manufacturer manufacturer){
        manufacturerRepository.save(manufacturer);

        return "Added Successfully";
    }

    public String saveManufacturers(ArrayList<Manufacturer> manufacturers){
        manufacturerRepository.saveAll(manufacturers);
        return "Added Successfully";
    }

    public String deleteManufacturer(int id){
        manufacturerRepository.deleteById(id);

        return "Deleted Successfully";
    }
}
