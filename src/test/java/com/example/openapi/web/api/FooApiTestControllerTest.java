package com.example.openapi.web.api;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
public class FooApiTestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class Get {
        @Test
        @DisplayName("success")
        public void getTest() throws Exception {
            final Long id = 1L;
            mockMvc.perform(get("/foo/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andReturn();
        }

        @Test
        @DisplayName("パラメータ無し")
        public void getTest_パラメータ無し() throws Exception {
            mockMvc.perform(get("/foo"))
                    .andExpect(status().is4xxClientError());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "0", "bar"})
        @DisplayName("パラメータ不正")
        public void getTest_パラメータ不正(String id) throws Exception {
            mockMvc.perform(get("/foo/{id}", id))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Post {
        @DisplayName("success")
        @Test
        public void postTest() throws Exception {
            final NewPet newPet = new NewPet();
            newPet.setName("foo");
            mockMvc.perform(post("/foo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newPet)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andDo(MockMvcResultHandlers.print())
            ;
        }
    }

}
