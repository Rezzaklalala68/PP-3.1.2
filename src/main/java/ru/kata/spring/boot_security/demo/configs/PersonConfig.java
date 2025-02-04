package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Configuration
public class PersonConfig {
    @Bean
    public CommandLineRunner initData(UserService userService, RoleRepository roleRepository) {
        return args -> {
            Role personRole = new Role();
            personRole.setName("ROLE_USER");
            roleRepository.save(personRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Person person = new Person();
            person.setFirstName("John");
            person.setLastName("Doe");
            person.setUsername("user");
            person.setPassword("123");
            person.setAge(30);
            person.setRoles(List.of(personRole));
            userService.add(person);

            Person admin = new Person();
            admin.setFirstName("Rob");
            admin.setLastName("Bobber");
            admin.setUsername("admin");
            admin.setPassword("123");
            admin.setAge(55);
            admin.setRoles(List.of(adminRole, personRole));
            userService.add(admin);
        };
    }
}
