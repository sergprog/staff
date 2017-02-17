package org.budnikov.staff.repository;

import org.budnikov.staff.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}



