package org.budnikov.staff.service;

import org.budnikov.staff.domain.Person;

import java.util.List;

public interface PersonService {

    List<Person> findAll();

    Person findById(Long id);

    Person save(Person person);

    void delete(Long id);
}
