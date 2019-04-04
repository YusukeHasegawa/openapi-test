package com.example.openapi.web.api;

import com.example.openapi.web.model.NewPet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

//import java.util.Map;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@AutoConfigureWebTestClient
public class PetsApiControllerTest2 {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void createPets() {
        webClient.post().uri("/pets").exchange().expectStatus().isCreated();
    }

    @Test
    public void createPets2() {
        final NewPet newPet = new NewPet();
        newPet.setName("foo");
        webClient.post()
                .uri("/pets2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(newPet))
                .exchange().expectStatus().isCreated();
    }

    @Test
    public void listPets() {
        webClient.get().uri("/pets").exchange().expectStatus().isOk();
    }

    @Test
    public void showPetById() {
        final EntityExchangeResult<List> res = webClient.get()
                .uri("/pets/{name}", "India")
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class).returnResult();
        assertThat(((Map) res.getResponseBody().get(0)).get("name")).isEqualTo("India");
    }
}