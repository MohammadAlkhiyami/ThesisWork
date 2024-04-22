package com.learnreactivespring.service;

import com.learnreactivespring.Dto.ItemsManufacturers;
import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.Manufacturer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final WebClient.Builder webClientBuilder;

    public DataService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<List<ItemsManufacturers>> getData() {
        Mono<List<Item>> itemsMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8085/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .collectList();

        Mono<List<Manufacturer>> manufacturersMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8086/manufacturers")
                .retrieve()
                .bodyToFlux(Manufacturer.class)
                .collectList();

        return Mono.zip(itemsMono, manufacturersMono)
                .map(tuple -> {
                    List<Item> items = tuple.getT1();
                    List<Manufacturer> manufacturers = tuple.getT2();
                    Map<String, Manufacturer> manufacturerMap = manufacturers.stream()
                            .collect(Collectors.toMap(Manufacturer::getId, Function.identity()));

                    List<ItemsManufacturers> mergedList = new ArrayList<>();
                    for (Item item : items) {
                        ItemsManufacturers mergedItem = new ItemsManufacturers();
                        mergedItem.setItemId(item.getId());
                        mergedItem.setItemDescription(item.getDescription());
                        mergedItem.setItemPrice(item.getPrice());
                        Manufacturer manufacturer = manufacturerMap.get(item.getManufacturerId());
                        if (manufacturer != null) {
                            mergedItem.setManufacturerId(manufacturer.getId());
                            mergedItem.setManufacturerName(manufacturer.getName());
                            mergedItem.setManufacturerAddress(manufacturer.getAddress());
                        }
                        mergedList.add(mergedItem);
                    }
                    return mergedList;
                });
    }

}