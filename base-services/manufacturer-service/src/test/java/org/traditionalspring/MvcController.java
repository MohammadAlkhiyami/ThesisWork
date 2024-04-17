package org.traditionalspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class MvcController {

    @GetMapping("/mvc/stream")
    public ResponseEntity<List<Long>> mvcStream() {
        // Handling an infinite stream in a synchronous MVC context is more complex.
        // This example just returns a list of Long values, simulating the behavior of Flux.interval(Duration.ofSeconds(1)).
        List<Long> longList = new ArrayList<>();
        longList.add(0L);
        longList.add(1L);
        longList.add(2L);
        return ResponseEntity.ok(longList);
    }

    // Traditional MVC Test
    @WebMvcTest(MvcController.class)
    static class MvcControllerTest {

        @Autowired
        private MockMvc mockMvc;

        void mvcStream() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/mvc/stream")
                            .contentType(MediaType.APPLICATION_STREAM_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            List<Long> longStreamList = parseResponseBody(mvcResult.getResponse().getContentAsString());

            // Simulating the verification of the received values
            assert longStreamList.size() == 3;
            assert longStreamList.get(0) == 0L;
            assert longStreamList.get(1) == 1L;
            assert longStreamList.get(2) == 2L;
        }

        private List<Long> parseResponseBody(String responseBody) {
            // Implement parsing logic based on the actual structure of the response body.
            // This could involve JSON parsing or other techniques.
            // Here, we read the content line by line and parse Long values.
            List<Long> longList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(responseBody.getBytes())))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    longList.add(Long.parseLong(line.trim()));
                }
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }
            return longList;
        }
    }
}
