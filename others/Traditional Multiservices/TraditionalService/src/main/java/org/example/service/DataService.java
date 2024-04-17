package org.example.service;

import org.example.Dto.ItemsManufacturers;
import org.example.domain.Item;
import org.example.domain.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private RestTemplate restTemplate; // or WebClient if you prefer

    public List<Item> fetchAllItems() {
        ResponseEntity<List<Item>> responseEntity = restTemplate.exchange(
                "http://localhost:8085/findAllItems", // Assuming "ItemService" is the registered name of the ItemService
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Item>>() {});

        return responseEntity.getBody();
    }

    public List<Manufacturer> fetchAllManufacturers() {
        ResponseEntity<List<Manufacturer>> responseEntity = restTemplate.exchange(
                "http://localhost:8086/findAllManufacturers", // Assuming "ManufacturerService" is the registered name of the ManufacturerService
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Manufacturer>>() {});

        return responseEntity.getBody();
    }

    public List<ItemsManufacturers> mergeLists(List<Item> items, List<Manufacturer> manufacturers) {
        List<ItemsManufacturers> mergedList = new ArrayList<>();

        for (Item item : items) {
            ItemsManufacturers mergedItem = new ItemsManufacturers();
            mergedItem.setItemId(item.getId());
            mergedItem.setItemDescription(item.getDescription());
            mergedItem.setItemPrice(item.getPrice());
            mergedItem.setManufacturerId(item.getManufacturerId());

            for (Manufacturer manufacturer : manufacturers) {
                if (item.getManufacturerId().equals(manufacturer.getId())) {
                    mergedItem.setManufacturerName(manufacturer.getName());
                    mergedItem.setManufacturerAddress(manufacturer.getAddress());
                    break;
                }
            }

            mergedList.add(mergedItem);
        }

        return mergedList;
    }
}
