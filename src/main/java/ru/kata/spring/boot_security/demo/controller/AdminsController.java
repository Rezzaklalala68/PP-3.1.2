package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService; // Внедряем интерфейс
    private final RoleService roleService; // Внедряем интерфейс
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminsController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("allPeople", userService.getUsers ());
        return "admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser (@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", userService.findPersonById(id));
        model.addAttribute("allRoles", roleService.getRoleList());

        return "update";
    }

    @PostMapping("update")
    public String updateUser ( @Validated @ModelAttribute("person")  Person person, BindingResult bindingResult,Model model,
                              @RequestParam("roleList") List<String> role ) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoleList());
            return "update";
        }

        // Проверка уникальности username
        Person existingPerson = (Person) userService.loadUserByUsername(person.getUsername());
        if (existingPerson != null && !existingPerson.getId().equals(person.getId())) {
            model.addAttribute("usernameError", "Username already exists");
            model.addAttribute("allRoles", roleService.getRoleList());
            return "update";
        }




        // Установка ролей и обновление пользователя
        person.setRoles(userService.getSetOfRoles(role));
        userService.update(person);

        return "redirect:/admin";
    }

    @GetMapping("add")
    public String registration(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("allRoles", roleService.getRoleList());
        return "add";
    }

    @PostMapping("add")
    public String addUser (@Validated @ModelAttribute("person")  Person person, BindingResult bindingResult,
                           @RequestParam(value = "role", required = false) List<String> role, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoleList());
            return "add";
        }
        try {
            if (role != null && !role.isEmpty()) {
                Collection<Role> roleList = userService.getSetOfRoles(role);
                person.setRoles(roleList);
            }
            userService.add(person);
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameError", e.getMessage());
            model.addAttribute("allRoles", roleService.getRoleList());
            return "add";
        }
        return "redirect:/admin";
    }
}
