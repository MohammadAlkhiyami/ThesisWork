package org.example.controller;

import org.example.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@Slf4j
public class ItemClientController {
    @Autowired
    ItemService itemService;

    @PostMapping("/items")
    public String saveItem(@RequestBody Item item){
        return itemService.saveItem(item);
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        return itemService.getItems();
    }

    @GetMapping("/items/{id}")
    public Optional<Item> getItem(@PathVariable String id) {

        if(itemService.getItem(id).isPresent()){
            return itemService.getItem(id);
        }
        return null;
    }
}