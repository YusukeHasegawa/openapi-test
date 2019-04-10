package com.example.openapi.service;

import com.example.openapi.web.model.NewPet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@SpringBootTest
class PetsServiceTest {

    @Autowired
    PetsService petsService;

    @Test
    void saveNewPet() {
        final NewPet newPet = new NewPet();
        newPet.setName("foo");
        assertThat(petsService.saveNewPet(newPet).getId()).isNotNull();
    }

    @Test
    void getPet() {
        final Long id = 1L;
        System.out.println(petsService.getPet(id));
        assertThat(petsService.getPet(id)).isNotNull();
    }
}