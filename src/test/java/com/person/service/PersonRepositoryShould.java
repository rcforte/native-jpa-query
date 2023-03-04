package com.person.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryShould {
  @Autowired
  PersonRepository personRepository;
  Person person;

  @BeforeEach
  void init() {
    person = Person.builder()
        .firstName("Rafael")
        .lastName("Forte")
        .build();
    personRepository.save(person);
  }

  @Test
  void find_person_after_saving() {
    var byId = personRepository.findById(person.getId()).get();
    assertThat(byId.getId()).isEqualTo(person.getId());
    assertThat(byId.getFirstName()).isEqualTo(person.getFirstName());
    assertThat(byId.getLastName()).isEqualTo(person.getLastName());
  }

  @Test
  void find_person_by_id_native() {
    var byId = personRepository.foo(person.getId());
    assertThat(byId.getId()).isEqualTo(person.getId());
    assertThat(byId.getFirstName()).isEqualTo(person.getFirstName());
    assertThat(byId.getLastName()).isEqualTo(person.getLastName());
  }
}
