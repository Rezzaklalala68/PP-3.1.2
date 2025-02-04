package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;
import java.util.List;
@Controller
public class AdminsController {
    private final UserService userService; // Внедряем интерфейс
    private final RoleService roleService; // Внедряем интерфейс
    private static final Logger logger = LoggerFactory.getLogger(AdminsController.class);
    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService; // Внедрение зависимости через интерфейс
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("allPeople", userService.getUsers ());
        logger.info("Successfully getAllUsers");
        return "admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser (@PathVariable("id") Long id) {
        userService.delete(id);
        logger.info("Successfully deleted user with id: " + id);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findPersonById(id));
        model.addAttribute("allRoles", roleService.getRoleList());
        logger.info("Successfully updateUserForm");
        return "update";
    }

    @PostMapping("/update")
    public String updateUser (@ModelAttribute("user") Person person, @RequestParam("roleList") List<String> role) {
        person.setRoles(userService.getSetOfRoles(role));
        userService.update (person);
        logger.info("Successfully updated user with id: " + person.getId());
        return "redirect:/admin";
    }

    @GetMapping("add")
    public String registration(Model model) {
        model.addAttribute("user", new Person());
        model.addAttribute("allRoles", roleService.getRoleList());
        logger.info("Successfully registration");
        return "add";
    }

    @PostMapping("/add")
    public String addUser (@ModelAttribute("user") Person person, @RequestParam(value = "role", required = false) List<String> role) {
        if (role != null && !role.isEmpty()) {
            Collection<Role> roleList = userService.getSetOfRoles(role);
            person.setRoles(roleList);
        }
        userService.add (person);
        logger.info("Successfully added user with id: " + person.getId());
        return "redirect:/admin";
    }
}
