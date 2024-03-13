package com.coussy.docker.microservice1.api;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerEndToEndTest {

    private MockWebServer mockWebServer;

    private static int HOST_PORT = 8097;

    @Autowired
    MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("microservice2.url", () -> "http://localhost:%s/api/v1".formatted(HOST_PORT));
    }

    @BeforeEach
    private void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(HOST_PORT);
        mockWebServer.getHostName();
    }

    @AfterEach
    private void end() throws IOException {
        mockWebServer.shutdown();
    }

    // TODO c'est une bonne pratique ce truc ? l'exception..
    @Test
    public void first() throws Exception {

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("\"service microservice2 available.\"");

        mockWebServer.enqueue(mockResponse);

        RequestBuilder requestBuilder = get("/api/v1/call/microservice2").accept(MediaType.ALL);
        String contentAsString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected =  "From microservice1 : expression returned from microservice2 \n "
                + "service microservice2 available."
                + "\n";
        Assertions.assertEquals(expected, contentAsString);

    }

}