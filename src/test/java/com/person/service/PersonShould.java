package com.person.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonShould {

  @Test
  public void be_created_with_builder() {
    var person = Person.builder()
        .id(1L)
        .firstName("Rafael")
        .lastName("Forte")
        .build();
    assertThat(person.getId()).isEqualTo(1);
    assertThat(person.getFirstName()).isEqualTo("Rafael");
    assertThat(person.getLastName()).isEqualTo("Forte");
  }
}
