package com.learnreactivespring.service;

import com.learnreactivespring.Dto.ItemsManufacturers;
import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.Manufacturer;
import com.learnreactivespring.repository.ItemReactiveRepository;
import com.learnreactivespring.repository.ManufacturerReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DataService {

    private final ItemReactiveRepository itemRepository;
    private final ManufacturerReactiveRepository manufacturerRepository;

    public DataService(ItemReactiveRepository itemRepository, ManufacturerReactiveRepository manufacturerRepository) {
        this.itemRepository = itemRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    public Flux<ItemsManufacturers> getData() {
        Flux<Item> itemsFlux = itemRepository.findAll();
        Flux<Manufacturer> manufacturersFlux = manufacturerRepository.findAll();

        return Flux.zip(itemsFlux, manufacturersFlux)
                .map(tuple -> {
                    Item item = tuple.getT1();
                    Manufacturer manufacturer = tuple.getT2();
                    ItemsManufacturers itemsManufacturers = new ItemsManufacturers();
                    itemsManufacturers.setItemId(item.getId());
                    itemsManufacturers.setItemDescription(item.getDescription());
                    itemsManufacturers.setItemPrice(item.getPrice());
                    itemsManufacturers.setManufacturerId(item.getManufacturerId());
                    itemsManufacturers.setManufacturerName(manufacturer.getName());
                    itemsManufacturers.setManufacturerAddress(manufacturer.getAddress());
                    return itemsManufacturers;
                });
    }
}
