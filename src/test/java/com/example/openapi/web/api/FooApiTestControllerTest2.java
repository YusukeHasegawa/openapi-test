package com.example.openapi.web.api;

import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FooApiTestControllerTest2 {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("success")
    public void getTest() throws Exception {
        final EntityExchangeResult<Pet> res = webClient.get()
                .uri("/foo/{name}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pet.class).returnResult();
        assertThat(res.getResponseBody().getId())
                .isEqualTo(1L);
    }

}
