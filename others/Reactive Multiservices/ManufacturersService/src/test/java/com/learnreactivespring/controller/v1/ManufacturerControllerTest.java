package com.learnreactivespring.controller.v1;

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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ManufacturerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    public List<Manufacturer> data() {

        return null;/*Arrays.asList(new Manufacturer(null, "Samsung TV", "Miskolc Centrum1"),
                new Manufacturer(null, "LG TV", "Miskolc Centrum2"),
                new Manufacturer(null, "Apple Watch", "Miskolc Centrum3"),
                new Manufacturer("ABC", "Beats HeadPhones", "Miskolc Centrum4"));*/
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
        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Manufacturer.class)
                .hasSize(4);

    }

    @Test
    public void getAllManufacturers_approach2(){
        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Manufacturer.class)
                .hasSize(4)
        .consumeWith((response) -> {
             List<Manufacturer> manufacturers =  response.getResponseBody();
            manufacturers.forEach((manufacturer) -> {
                assertTrue(manufacturer.getId()!=null);
            });

        });
    }

    @Test
    public void getAllManufacturers_approach3(){


        Flux<Manufacturer> manufacturersFlux = webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Manufacturer.class)
                .getResponseBody();

        StepVerifier.create(manufacturersFlux.log("value from network : "))
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getOneManufacturer(){

        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1.concat("/{id}"),"ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.address", "Miskolc Centrum 1");

    }

    @Test
    public void getOneManufacturers_notFound(){

        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1.concat("/{id}"),"DEF")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void createManufacturers(){


        Manufacturer manufacturer = new Manufacturer(null, "Iphone X", "Miskolc Centrum 1");

        webTestClient.post().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(manufacturer), Manufacturer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone X")
                .jsonPath("$.address").isEqualTo("Miskolc Centrum 1");



    }

    @Test
    public void deleteManufacturers(){

        webTestClient.delete().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1.concat("/{id}"),"ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

    }

    @Test
    public void updateManufacturers(){
        String newAddress ="Miskolc Centrum 1";
        Manufacturer manufacturer = new Manufacturer(null,"Beats HeadPhones", newAddress);

        webTestClient.put().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1.concat("/{id}"),"ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(manufacturer), Manufacturer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.address",newAddress);

    }

    @Test
    public void updateManufacturers_notFound(){
        String newAddress ="Miskolc Centrum 1";
        Manufacturer manufacturer = new Manufacturer(null,"Beats HeadPhones", newAddress);

        webTestClient.put().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1.concat("/{id}"),"DEF")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(manufacturer), Manufacturer.class)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void runTimeException(){
        webTestClient.get().uri(ManufacturersConstants.MANUFACTURERS_END_POINT_V1+"/runtimeException")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("RuntimeException Occurred.");



    }

}
