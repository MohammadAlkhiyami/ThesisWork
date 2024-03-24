package com.learnreactivespring.initialize;

import com.learnreactivespring.document.Manufacturer;
import com.learnreactivespring.document.ManufacturerCapped;
import com.learnreactivespring.repository.ManufacturerReactiveCappedRepository;
import com.learnreactivespring.repository.ManufacturerReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
@Slf4j
public class ManufacturerDataInitializer /*implements CommandLineRunner*/ {

    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    @Autowired
    ManufacturerReactiveCappedRepository manufacturerReactiveCappedRepository;

    @Autowired
    ReactiveMongoOperations mongoOperations;

    /*@Override
    public void run(String... args) throws Exception {

        initalDataSetUp();
        createCappedCollection();
        dataSetUpforCappedCollection();
    }*/

    private void createCappedCollection() {
        mongoOperations.dropCollection(ManufacturerCapped.class)
        .then(mongoOperations.createCollection(ManufacturerCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped()));

    }

    public List<Manufacturer> data() {
        List<Manufacturer> manufacturers = new ArrayList<>();

        /*return Arrays.asList(new Manufacturer(null, "Samsung TV", 399.99),
                new Manufacturer(null, "LG TV", 329.99),
                new Manufacturer(null, "Apple Watch", 349.99),
                new Manufacturer("ABC", "Beats HeadPhones", 149.99));*/
        return manufacturers;
    }

    public void dataSetUpforCappedCollection(){

        Flux<ManufacturerCapped> manufacturerCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ManufacturerCapped(null,"Random Manufacturer " + i, "Miskolc Centrum"));

        manufacturerReactiveCappedRepository
                .insert(manufacturerCappedFlux)
                .subscribe((manufacturerCapped -> {
                    log.info("Inserted Manufacturer is " + manufacturerCapped);
                }));

    }

    private void initalDataSetUp() {

        manufacturerReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                        .flatMap(manufacturerReactiveRepository::save)
                        .thenMany(manufacturerReactiveRepository.findAll())
                        .subscribe((manufacturer -> {
                            System.out.println("Manufacturer inserted from CommandLineRunner : " + manufacturer);
                        }));

    }



}
