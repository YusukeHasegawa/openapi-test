package com.example.openapi.web.api;

import com.example.openapi.web.model.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FooApiTestControllerTest2 {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("success")
    public void getTest() {
        final EntityExchangeResult<Pet> res = webClient.get()
                .uri("/foo/{name}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pet.class).returnResult();
        assertThat(res.getResponseBody().getId())
                .isEqualTo(1L);
    }
}

