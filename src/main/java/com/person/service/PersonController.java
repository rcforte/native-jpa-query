package com.person.service;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PersonController {
  private final PersonRepository personRepository;

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @QueryMapping
  public List<Person> all() {
    return personRepository.findAll();
  }

  @QueryMapping
  public Person byId(@Argument Long id) {
    return personRepository.findById(id).get();
  }
}
