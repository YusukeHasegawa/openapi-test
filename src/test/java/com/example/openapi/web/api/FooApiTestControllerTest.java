package com.example.openapi.web.api;

import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tracing.dtrace.ProviderAttributes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
            mockMvc.perform(get("/foo/"))
                    .andExpect(status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print());
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

    @Nested
    class Lists {
        @DisplayName("パラメータの件数分Petのリストを返却する")
        @Test
        public void getTest() throws Exception {
            final MvcResult result = mockMvc.perform(get("/foo").param("limit", "2"))
                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.length()").value(2))
//                    .andExpect(jsonPath("$[0].name").isNotEmpty())
//                    .andExpect(jsonPath("$[0].id").isNotEmpty())
//                    .andExpect(jsonPath("$[0].tag").hasJsonPath())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            final Pet[] pets = objectMapper.readValue(result.getResponse().getContentAsString(), Pet[].class);
            assertThat(pets).hasSize(2);
            assertThat(pets[0].getName()).isNotBlank();
            assertThat(pets[0].getId()).isNotNull();
        }

        @DisplayName("パラメータが 1 <= limit <= 500 以外の時400")
        @ParameterizedTest
        @ValueSource(strings = {"0", "501"})
        public void invalidParamTest(String limit) throws Exception {
            mockMvc.perform(get("/foo").param("limit", limit))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("パラメータがない場合は1件")
        @Test
        public void defaultParamTest() throws Exception {
            mockMvc.perform(get("/foo"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
            ;
        }
    }

    @Nested
    class Delete {

        @DisplayName("idを指定して削除する")
        @Test
        public void deleteTest() throws Exception {
            Long id = 2L;
            mockMvc.perform(delete("/foo/{id}", id))
                    .andExpect(status().isNoContent())
                    .andDo(MockMvcResultHandlers.print())
            ;
        }

        @DisplayName("idのバリデーション")
        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "foo", ""})
        public void deleteTest(String id) throws Exception {
            mockMvc.perform(delete("/foo/{id}", id))
                    .andExpect(status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print())
            ;
        }
    }
}
