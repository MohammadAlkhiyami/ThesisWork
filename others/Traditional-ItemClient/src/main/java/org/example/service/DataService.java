package org.example.service;

import org.example.Dto.ItemsManufacturers;
import org.example.domain.Item;
import org.example.domain.Manufacturer;
import org.example.repository.ItemRepository;
import org.example.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<Item> fetchDataFromSource1() {
        return itemRepository.findAll(); // Assuming findAll() fetches all items from the repository
    }

    public List<Manufacturer> fetchDataFromSource2() {
        return manufacturerRepository.findAll(); // Assuming findAll() fetches all manufacturers from the repository
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
