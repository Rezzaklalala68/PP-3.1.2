package ru.kata.spring.boot_security.demo.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "roleName should not be empty")
    @Size(min = 2, max = 35, message = "roleName should be between 2 and 35 characters")
    private String name;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<Person> people;
    public Role() {
    }
    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getUsers() {
        return people;
    }

    public void setUsers(Set<Person> people) {
        this.people = people;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
