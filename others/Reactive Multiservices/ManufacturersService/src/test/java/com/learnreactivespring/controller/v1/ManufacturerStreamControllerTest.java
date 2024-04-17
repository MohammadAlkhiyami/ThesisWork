package com.learnreactivespring.controller.v1;

import com.learnreactivespring.constants.ManufacturersConstants;
import com.learnreactivespring.document.ManufacturerCapped;
import com.learnreactivespring.repository.ManufacturerReactiveCappedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ManufacturerStreamControllerTest {

    @Autowired
    ManufacturerReactiveCappedRepository manufacturerReactiveCappedRepository;

    @Autowired
    ReactiveMongoOperations mongoOperations;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void setUp() {

        mongoOperations.dropCollection(ManufacturerCapped.class);
        mongoOperations.createCollection(ManufacturerCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped())
                .block();

        Flux<ManufacturerCapped> manufacturerCappedFlux = Flux.interval(Duration.ofMillis(100))
                .map(i -> new ManufacturerCapped(null, "Random Manufacturer " + i, "Miskolc Centrum 1"))
                .take(5);

        manufacturerReactiveCappedRepository
                .insert(manufacturerCappedFlux)
                .doOnNext((manufacturerCapped -> {
                    System.out.println("Inserted Manufacturer in setUp " + manufacturerCapped);
                }))
                .blockLast();


    }

    @Test
    public void testStreamAllManufacturers() {

        Flux<ManufacturerCapped> manufacturerCappedFlux = webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_STREAM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .returnResult((ManufacturerCapped.class))
                .getResponseBody()
                .take(5);

        StepVerifier.create(manufacturerCappedFlux)
                .expectNextCount(5)
                .thenCancel()
                .verify();

    }


}
