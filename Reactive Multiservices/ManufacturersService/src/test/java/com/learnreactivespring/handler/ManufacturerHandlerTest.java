package com.learnreactivespring.handler;

import com.learnreactivespring.constants.ManufacturersConstants;
import com.learnreactivespring.document.Manufacturer;
import com.learnreactivespring.repository.ManufacturerReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ManufacturerHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    public List<Manufacturer> data() {

        /*return Arrays.asList(new Manufacturer(null, "Samsung TV", "399.99"),
                new Manufacturer(null, "LG TV", "399.99"),
                new Manufacturer(null, "Apple Watch", "399.99"),
                new Manufacturer("ABC", "Beats HeadPhones", "399.99"));*/
                return null;
    }


    @Before
    public void setUp(){
        manufacturerReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(manufacturerReactiveRepository::save)
                .doOnNext((manufacturer -> {
                    System.out.println("Inserted manufacturer is : " + manufacturer);
                }))
                .blockLast();
    }

    @Test
    public void getAllManufacturers(){
        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Manufacturer.class)
                .hasSize(4);

    }

    @Test
    public void getOneManufacturer(){

        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_FUNCTIONAL_END_POINT_V1.concat("/{id}"),"ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 149.99);

    }

    @Test
    public void getOneManufacturer_notFound(){

        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_FUNCTIONAL_END_POINT_V1.concat("/{id}"),"DEF")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void runTimeException(){

        webTestClient.get().uri("/fun/runtimeexception")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message","RuntimeException Occurred");
    }

}
