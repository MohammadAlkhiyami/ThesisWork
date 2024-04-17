package org.example.service;

import org.example.domain.Item;
import org.example.domain.Manufacturer;
import org.example.repository.ItemRepository;
import org.example.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, ManufacturerRepository manufacturerRepository) {
        this.itemRepository = itemRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<Item> fetchDataFromSource1() {
        return itemRepository.findAll();
    }

    public List<Manufacturer> fetchDataFromSource2() {
        return manufacturerRepository.findAll();
    }

    public Optional<Item> getItem(String id){
        return itemRepository.findById(id);
    }

    public String saveItem(Item item){
        itemRepository.save(item);

        return "Added Successfully";
    }

    public String saveItems(ArrayList<Item> items){
        itemRepository.saveAll(items);
        return "Added Successfully";
    }
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public String deleteItem(String id){
        itemRepository.deleteById(id);

        return "Deleted Successfully";
    }

}
