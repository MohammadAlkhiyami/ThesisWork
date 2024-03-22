package org.example.controller;

import org.example.Dto.ItemsManufacturers;
import org.example.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Manufacturer;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@Slf4j
public class ItemClientController {

    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    ItemService itemService;

    String baseUrl = "http://localhost:8080";

    @GetMapping("/client/retrieve")
    public Item[] getAllItemsUsingRetrieve() {
        return restTemplate.getForObject(baseUrl + "/v1/items", Item[].class);
    }

    @GetMapping("/client/exchange")
    public Item[] getAllItemsUsingExchange() {
        return restTemplate.getForObject(baseUrl + "/v1/items", Item[].class);
    }

    @GetMapping("/client/retrieve/singleItem/{id}")
    public Item getOneItemUsingRetrieve(@PathVariable String id) {
        return restTemplate.getForObject(baseUrl + "/v1/items/{id}", Item.class, id);
    }

    @GetMapping("/client/exchange/singleItem/{id}")
    public Item getOneItemUsingExchange(@PathVariable String id) {
        return restTemplate.getForObject(baseUrl + "/v1/items/{id}", Item.class, id);
    }

    @PostMapping("/client/createItem")
    public Item createItem(@RequestBody Item item) {
        return restTemplate.postForObject(baseUrl + "/v1/items", item, Item.class);
    }

    @DeleteMapping("/client/deleteItem/{id}")
    public void deleteItem(@PathVariable String id) {
        restTemplate.delete(baseUrl + "/v1/items/{id}", id);
    }

    @GetMapping("/client/exchange/error")
    public Item[] errorExchange() {
        return restTemplate.getForObject(baseUrl + "/v1/items/runtimeException", Item[].class);
    }

    @PutMapping("/client/updateItem/{id}")
    public void updateItem(@PathVariable String id, @RequestBody Item item) {
        restTemplate.put(baseUrl + "/v1/items/{id}", item, id);
    }

    @PostMapping("/addItem")
    public String saveItem(@RequestBody Item item){
        return itemService.saveItem(item);
    }

    @PostMapping("/addItems")
    public String saveItems(@RequestBody ArrayList<Item> items){
        return itemService.saveItems(items);
    }

    @GetMapping("/findAllItems")
    public List<Item> getItems() {

        return itemService.getItems();
    }

    @GetMapping("/getItem/{id}")
    public Optional<Item> getItem(@PathVariable String id) {

        if(itemService.getItem(id).isPresent()){
            return itemService.getItem(id);
        }
        return null;
    }

}