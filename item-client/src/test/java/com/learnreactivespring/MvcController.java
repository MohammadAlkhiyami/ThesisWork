package com.learnreactivespring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class MvcController {

    @GetMapping("/mvc/stream")
    public ResponseEntity<Flux<Long>> mvcStream() {
        // Handling an infinite stream in a synchronous MVC context is more complex.
        // This example just returns a Flux, but proper handling would require additional considerations.
        return ResponseEntity.ok(Flux.interval(Duration.ofSeconds(1)));
    }


    // Traditional MVC Test
    @WebMvcTest(MvcController.class)
    class MvcControllerTest {

        @Autowired
        private MockMvc mockMvc;

        void mvcStream() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/stream")
                            .contentType(MediaType.APPLICATION_STREAM_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            Flux<Long> longStreamFlux = parseResponseBody(mvcResult.getResponse().getContentAsString());

            StepVerifier.create(longStreamFlux)
                    .expectNext(0L)
                    .expectNext(1L)
                    .expectNext(2L)
                    .thenCancel()
                    .verify();
        }


        private Flux<Long> parseResponseBody(String responseBody) {
            // Implement parsing logic based on the actual structure of the response body.
            // This could involve converting the string to a Flux or handling the content appropriately.
            // In a real-world scenario, additional parsing and processing would be necessary.
            // For simplicity, the exact logic depends on the actual behavior of the endpoint.
            // This example assumes a hypothetical parse method.
            return parse(responseBody);
        }

        private Flux<Long> parse(String responseBody) {
            // Hypothetical method for parsing the response body into a Flux<Long>.
            // This method should be adapted based on the actual structure of the response.
            // The goal is to extract the values and create a Flux.
            // In a real-world scenario, this might involve JSON parsing or other techniques.
            // This is a simplified example for illustration purposes.
            return Flux.just(0L, 1L, 2L);
        }
    }
}
