package com.learnreactivespring.repository;

import com.learnreactivespring.document.Manufacturer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ManufacturerReactiveRepositorytest {


    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    List<Manufacturer> manufacturerList = null;/*Arrays.asList(new Manufacturer(null, "Samsung TV", "400.0"),
            new Manufacturer(null, "LG TV", "420.0"),
            new Manufacturer(null, "Apple Watch", "299.99"),
            new Manufacturer(null, "Beats Headphones", "149.99"),
            new Manufacturer("ABC", "Bose Headphones", "149.99"));*/

    @Before
    public void setUp() {

        manufacturerReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(manufacturerList))
                .flatMap(manufacturerReactiveRepository::save)
                .doOnNext((manufacturer -> {
                    System.out.println("Inserted Manufacturer is :" + manufacturer);
                }))
                .blockLast();

    }


    @Test
    public void getAllManufacturers() {

        StepVerifier.create(manufacturerReactiveRepository.findAll()) // 4
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getManufacturerByID() {

        StepVerifier.create(manufacturerReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((manufacturer -> manufacturer.getName().equals("Bose Headphones")))
                .verifyComplete();


    }

    @Test
    public void findManufacturerByDescrition() {

        StepVerifier.create(manufacturerReactiveRepository.findByName("Bose Headphones").log("findManufacturerByName : "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveManufacturer() {

        Manufacturer manufacturer = new Manufacturer(null, "Google Home Mini", "30.00");
        Mono<Manufacturer> savedManufacturer = manufacturerReactiveRepository.save(manufacturer);
        StepVerifier.create(savedManufacturer.log("save Manufacturer : "))
                .expectSubscription()
                .expectNextMatches(manufacturer1 -> (manufacturer1.getId() != null && manufacturer1.getName().equals("Google Home Mini")))
                .verifyComplete();

    }

    @Test
    public void updateManufacturer() {

        String newAddress = "Miskolc Centrum";
        Mono<Manufacturer> updatedManufacturer = manufacturerReactiveRepository.findByName("LG TV")
                .map(manufacturer -> {
                    manufacturer.setName(newAddress); //setting the new address
                    return manufacturer;
                })
                .flatMap((manufacturer) -> {
                    return manufacturerReactiveRepository.save(manufacturer); //saving the manufacturer with the new address
                });

        StepVerifier.create(updatedManufacturer)
                .expectSubscription()
                .expectNextMatches(manufacturer -> manufacturer.getAddress().equals("Miskolc Centrum"))
                .verifyComplete();


    }


    @Test
    public void deleteManufacturerById() {


        Mono<Void> deletedManufacturer = manufacturerReactiveRepository.findById("ABC") // Mono<Manufacturer>
                .map(Manufacturer::getId) // get Id -> Transform from one type to another type
                .flatMap((id) -> {
                    return manufacturerReactiveRepository.deleteById(id);
                });

        StepVerifier.create(deletedManufacturer.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(manufacturerReactiveRepository.findAll().log("The new Manufacturer List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }

    @Test
    public void deleteManufacturer() {


        Mono<Void> deletedManufacturer = manufacturerReactiveRepository.findByName("LG TV") // Mono<Manufacturer>
                .flatMap((manufacturer) -> {
                    return manufacturerReactiveRepository.delete(manufacturer);
                });

        StepVerifier.create(deletedManufacturer.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(manufacturerReactiveRepository.findAll().log("The new Manufacturer List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }



}
