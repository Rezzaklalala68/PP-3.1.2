package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.PersonRepository;
import ru.kata.spring.boot_security.demo.model.Person;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService; // Не забудьте добавить RoleService

    @Autowired
    public UserServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);
        if (person == null) {
            throw new UsernameNotFoundException(String.format("User  not found with username: " + username));
        }

        return person;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    @EntityGraph(attributePaths = {"roles"})
    public Person findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public List<Role> getSetOfRoles(List<String> rolesId) {
        List<Role> roleSet = new ArrayList<>();
        for (String id : rolesId) {
            roleSet.add(roleService.getRoleById(Long.parseLong(id)));
        }
        return roleSet;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<Person> getUsers () {
        return personRepository.findAll();
    }

    @Override
    public void add (Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    @Override
    public void update (Person person) {
        if (!person.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        } else {
            Person existingPerson  = personRepository.findById(person.getId()).orElse(null);
            if (existingPerson  != null) {
                person.setPassword(existingPerson.getPassword());
            }
        }
        personRepository.saveAndFlush(person);
    }

    @Override
    public Person findPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public void delete (Long id) {
        personRepository.deleteById(id);
    }


}
