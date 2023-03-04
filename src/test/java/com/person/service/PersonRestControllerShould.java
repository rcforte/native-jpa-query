package com.person.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonRestController.class)
class PersonRestControllerShould {
  @Autowired
  MockMvc mvc;
  @MockBean
  PersonRepository personRepository;

  @Test
  void find_all_persons() throws Exception {
    given(personRepository.findAll())
        .willReturn(persons());
    mvc.perform(
            get("/person/").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].firstName", is("Rafael")))
        .andExpect(jsonPath("$[0].lastName", is("Forte")));
  }

  @Test
  void find_person_by_id() throws Exception {
    given(personRepository.findById(1L))
        .willReturn(Optional.of(persons().get(0)));
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
    given(personRepository.foo(1L))
        .willReturn(persons().get(0));
    mvc.perform(
            get("/person/native/1").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Rafael")))
        .andExpect(jsonPath("$.lastName", is("Forte")));
  }

  private List<Person> persons() {
    return List.of(
        new Person(1L, "Rafael", "Forte"),
        new Person(2L, "Carmen", "Forte")
    );
  }
}
