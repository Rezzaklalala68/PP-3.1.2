package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Person;

import javax.validation.constraints.Size;
import java.util.Optional;
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByUsername(@Size(min=2, message = "Не меньше 5 знаков") String username);

}
