package com.example.openapi.web.api;

import com.example.openapi.domain.Pets;
import com.example.openapi.repository.PetsRepository;
import com.example.openapi.web.mapper.PetMapper;
import com.example.openapi.web.model.NewPet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
public class PetsApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    //
    @MockBean
    // @SpyBean
            PetsRepository petsRepository;

    @Autowired
    PetMapper petMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("ペットの作成")
    public void createPets() throws Exception {
        given(petsRepository.save(new Pets().setName("taro 1"))).willAnswer(invocation -> {
            final Pets p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        mockMvc.perform(post("/pets")).andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        then(petsRepository).should(times(1)).save(any());
    }

    @Test
    public void createPets2() throws Exception {
        final NewPet newPet = new NewPet();
        newPet.setName("foo");
        final Pets pets = petMapper.newPetToPets(newPet);
        given(petsRepository.save(pets)).willAnswer(invocation -> {
            final Pets p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        mockMvc.perform(post("/pets2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPet)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        then(petsRepository).should(times(1)).save(new Pets().setId(1L).setName("foo"));
    }

    @Nested
    @DisplayName("ペットリスト")
    class ListPets {
        @Test
        public void listPets() throws Exception {
            mockMvc.perform(get("/pets")).andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"101", "0"})
        public void listPets_limit(String limit) throws Exception {
            mockMvc.perform(get("/pets").param("limit", limit))
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void showPetById() throws Exception {
        final List<Pets> pets = new ArrayList<>();
        pets.add(new Pets(1L, "India", null));
        given(petsRepository.findByName("India")).willReturn(pets);

        mockMvc.perform(get("/pets/{name}", "India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("India"))
                .andDo(MockMvcResultHandlers.print());
    }

}