package com.learnreactivespring.service;

import com.learnreactivespring.Dto.ItemsManufacturers;
import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.Manufacturer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class DataService {

    private final WebClient.Builder webClientBuilder;

    public DataService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Flux<ItemsManufacturers> getData() {
        Flux<Item> itemsFlux = webClientBuilder.build()
                .get()
                .uri("http://localhost:8085/items")
                .retrieve()
                .bodyToFlux(Item.class);

        // request is sent but response is not neccessary
        Flux<Manufacturer> manufacturersFlux = webClientBuilder.build()
                .get()
                .uri("http://localhost:8086/manufacturers")
                .retrieve()
                .bodyToFlux(Manufacturer.class);

        // waiting for the responses of both request
        return Flux.zip(itemsFlux, manufacturersFlux)
                .map(tuple -> {
                    Item item = tuple.getT1();
                    Manufacturer manufacturer = tuple.getT2();
                    ItemsManufacturers itemsManufacturers = new ItemsManufacturers();
                    itemsManufacturers.setItemId(item.getId());
                    itemsManufacturers.setItemDescription(item.getDescription());
                    itemsManufacturers.setItemPrice(item.getPrice());
                    itemsManufacturers.setManufacturerId(manufacturer.getId());
                    itemsManufacturers.setManufacturerName(manufacturer.getName());
                    itemsManufacturers.setManufacturerAddress(manufacturer.getAddress());
                    return itemsManufacturers;
                });
    }
}