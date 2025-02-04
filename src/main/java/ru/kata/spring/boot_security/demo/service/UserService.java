package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService  {
    List<Person> getUsers();
    Person findPersonById(Long id);
    void add(Person person);
    void update(Person person);
    void delete(Long id);
    Collection<Role> getSetOfRoles(List<String> role);
    String getCurrentUsername();


}
