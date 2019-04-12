package com.example.openapi.repository;

import com.example.openapi.domain.Pets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJUnitConfig
@DataJpaTest
public class PetsRepositoryTest {

    @Autowired
    PetsRepository petsRepository;

    @Test
    public void test() {
        final Pets entity = new Pets();
        entity.setName("beer");
        petsRepository.save(entity);

        final List<Pets> petList = petsRepository.findByName("beer");
        assertThat(petList).size().isEqualTo(1);
        assertThat(petList.get(0).getName()).isEqualTo("beer");

    }

    @Test
    public void saveTest(){
        final Pets pets = new Pets();
        pets.setName("foo");
        petsRepository.save(pets);
        assertThat(pets.getId()).isNotNull();
    }

    @BeforeEach
    public void beforeEach(){
        System.out.println("before each");
    }

    @BeforeAll
    public static void beforeAll(){
        System.out.println("before all");
    }
}
