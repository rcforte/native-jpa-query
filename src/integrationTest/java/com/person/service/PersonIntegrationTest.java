package com.person.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK, classes = PersonMicroservice.class)
@AutoConfigureMockMvc
class PersonIntegrationTest {
  @Autowired
  MockMvc mvc;

  @Autowired
  PersonRepository personRepository;

  @BeforeEach
  void init() {
    personRepository.save(
        Person.builder()
            .firstName("Rafael")
            .lastName("Forte")
            .build()
    );
  }

  @Test
  void find_all_persons() throws Exception {
    mvc.perform(
            get("/person/").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize((int) personRepository.count())))
        .andExpect(jsonPath("$[ 0 ].firstName", is("Rafael")));
  }

  @Test
  void find_person_by_id() throws Exception {
    mvc.perform(
            get("/person/1").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Rafael")))
        .andExpect(jsonPath("$.lastName", is("Forte")));
  }

  @Test
  void find_person_by_id_using_native_query() throws Exception {
    mvc.perform(
            get("/person/native/1").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Rafael")))
        .andExpect(jsonPath("$.lastName", is("Forte")));
  }
}
