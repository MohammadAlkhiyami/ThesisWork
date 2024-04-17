package com.learnreactivespring.handler;

import com.learnreactivespring.document.Manufacturer;
import com.learnreactivespring.document.ManufacturerCapped;
import com.learnreactivespring.repository.ManufacturerReactiveCappedRepository;
import com.learnreactivespring.repository.ManufacturerReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ManufacturersHandler {

    @Autowired
    ManufacturerReactiveRepository manufacturerReactiveRepository;

    @Autowired
    ManufacturerReactiveCappedRepository manufacturerReactiveCappedRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllManufacturers(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(manufacturerReactiveRepository.findAll(), Manufacturer.class);

    }

    public Mono<ServerResponse> getOneManufacturer(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        Mono<Manufacturer> manufacturerMono = manufacturerReactiveRepository.findById(id);

        return manufacturerMono.flatMap(manufacturer ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(manufacturer)))
                .switchIfEmpty(notFound);

    }

    public Mono<ServerResponse> createManufacturer(ServerRequest serverRequest) {

        Mono<Manufacturer> manufacturerTobeInserted = serverRequest.bodyToMono(Manufacturer.class);

        return manufacturerTobeInserted.flatMap(manufacturer ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(manufacturerReactiveRepository.save(manufacturer), Manufacturer.class));

    }

    public Mono<ServerResponse> deleteManufacturer(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        Mono<Void> deleteManufacturer = manufacturerReactiveRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteManufacturer, Void.class);
    }

    public Mono<ServerResponse> updateManufacturer(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        Mono<Manufacturer> updatedManufacturer = serverRequest.bodyToMono(Manufacturer.class)
                .flatMap((manufacturer) -> {

                    Mono<Manufacturer> manufacturerMono = manufacturerReactiveRepository.findById(id)
                            .flatMap(currentManufacturer -> {
                                currentManufacturer.setName(manufacturer.getName());
                                currentManufacturer.setAddress(manufacturer.getAddress());
                                return manufacturerReactiveRepository.save(currentManufacturer);

                            });
                    return manufacturerMono;
                });

        return updatedManufacturer.flatMap(manufacturer ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(manufacturer)))
                .switchIfEmpty(notFound);


    }

    public Mono<ServerResponse> manufacturersStream(ServerRequest serverRequest){

        return  ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(manufacturerReactiveCappedRepository.findManufacturersBy(), ManufacturerCapped.class);
    }

    public Mono<ServerResponse> manufacturersEx(ServerRequest serverRequest){

        throw new RuntimeException("RuntimeException Occurred");
    }


}
