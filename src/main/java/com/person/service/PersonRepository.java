package com.person.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PersonRepository extends JpaRepository<Person, Long> {
  @Query(name = "Person.queryById", nativeQuery = true)
  Person foo(@Param("id") Long id);
}
