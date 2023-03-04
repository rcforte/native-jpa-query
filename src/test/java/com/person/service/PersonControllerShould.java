package com.person.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@GraphQlTest(PersonController.class)
class PersonControllerShould {
  @Autowired
  GraphQlTester client;

  @MockBean
  PersonRepository personRepository;

  @Test
  public void find_all_persons() {
    given(personRepository.findAll())
        .willReturn(
            List.of(
                Person.builder()
                    .id(1L)
                    .firstName("Rafael")
                    .lastName("Forte")
                    .build(),
                Person.builder()
                    .id(2L)
                    .firstName("Carmen")
                    .lastName("Forte")
                    .build()
            )
        );

    // language=GraphQL
    var document = """
        query {
          all {
            id
            firstName
            lastName
          }
        }
        """;
    client.document(document)
        .execute()
        .path("all")
        .entityList(Person.class)
        .hasSize(2);
  }

  @Test
  public void find_person_by_id() {
    given(personRepository.findById(1L))
        .willReturn(
            Optional.of(
                Person.builder()
                    .id(1L)
                    .firstName("Rafael")
                    .lastName("Forte")
                    .build()
            )
        );

    // language=GraphQL
    var document = """
        query byId($id: ID!) {
          byId(id: $id) {
            id
            firstName
            lastName
          }
        }
        """;
    client.document(document)
        .variable("id", 1L)
        .execute()
        .path("byId")
        .entity(Person.class)
        .satisfies(person -> {
          assertThat(person.getId()).isEqualTo(1L);
          assertThat(person.getFirstName()).isEqualTo("Rafael");
          assertThat(person.getLastName()).isEqualTo("Forte");
        });
  }
}
