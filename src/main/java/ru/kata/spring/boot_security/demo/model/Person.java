package ru.kata.spring.boot_security.demo.model;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;



@Entity
@Table(name = "people")
public class Person implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private Long id;

    @Column
    @NotBlank(message = "firstName should not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "firstName should contain only letters")
    @Size(min = 2, max = 35, message = "firstName should be between 2 and 35 characters")
    private String firstName;


    @Column
    @NotBlank(message = "lastName should not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "lastName should contain only letters")
    @Size(min = 2, max = 35, message = "lastName should be between 2 and 35 characters")
    private String lastName;

    @Column(name = "age")
    @Min(value = 1, message = "Age should be greater than 0")
    private int age;

    @Size(min=2, message = "Не меньше 2 знаков / введите уникальный логин")
    @Column(unique = true )
    private String username;

    @Size(min=2, message = "Не меньше 2 знаков")
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name = "person_roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public Person(){

    }

    public Person(Long id, String firstName, String lastName, int age, String passwordConfirm, String userName, String password, Collection<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.passwordConfirm = passwordConfirm;
        this.username = userName;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {return id;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Collection<Role> getRoles() {
        return roles;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public int getAge() {
        return age;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(username, person.username) && Objects.equals(password, person.password) && Objects.equals(passwordConfirm, person.passwordConfirm) && Objects.equals(roles, person.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, username, password, passwordConfirm, roles);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", roles=" + roles +
                '}';
    }
}
