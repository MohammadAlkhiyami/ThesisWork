package com.learnreactivespring.router;

import com.learnreactivespring.handler.ManufacturersHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.learnreactivespring.constants.ManufacturersConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ManufacturersRouter {

    @Bean
    public RouterFunction<ServerResponse> manufacturersRoute(ManufacturersHandler manufacturersHandler){

        return RouterFunctions
                .route(GET(MANUFACTURERS_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON))
                ,manufacturersHandler::getAllManufacturers)
                .andRoute(GET(MANUFACTURERS_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(APPLICATION_JSON))
                ,manufacturersHandler::getOneManufacturer)
                .andRoute(POST(MANUFACTURERS_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON))
                ,manufacturersHandler::createManufacturer)
                .andRoute(DELETE(MANUFACTURERS_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(APPLICATION_JSON))
                        ,manufacturersHandler::deleteManufacturer)
                .andRoute(PUT(MANUFACTURERS_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(APPLICATION_JSON))
                        ,manufacturersHandler::updateManufacturer);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(ManufacturersHandler manufacturersHandler){
        return RouterFunctions
                .route(GET("/fun/runtimeexception").and(accept(APPLICATION_JSON))
                        ,manufacturersHandler::manufacturersEx);

    }

    @Bean
    public RouterFunction<ServerResponse> manufacturerStreamRoute(ManufacturersHandler manufacturersHandler){

        return RouterFunctions
                .route(GET(MANUFACTURERS_STREAM_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON))
                        ,manufacturersHandler::manufacturersStream);

    }

}
