package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Person;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Person findByUsername(@Size(min=2, message = "More 2 elements / Not unique username")  String username);

}
