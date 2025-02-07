package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService; // Внедрение зависимости через интерфейс
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
    public String updateUser ( @Valid @ModelAttribute("person")  Person person, BindingResult bindingResult,
                              @RequestParam("roleList") List<String> role) {
        if(bindingResult.hasErrors()) {
            return "update";
        }

        person.setRoles(userService.getSetOfRoles(role));
        userService.update (person);
        return "redirect:/admin";
    }

    @GetMapping("add")
    public String registration(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("allRoles", roleService.getRoleList());
        return "add";
    }

    @PostMapping("add")
    public String addUser (@Valid @ModelAttribute("person")  Person person, BindingResult bindingResult,
                           @RequestParam(value = "role", required = false) List<String> role) {
        if(bindingResult.hasErrors()) {
            return "add";
        }
        if (role != null && !role.isEmpty()) {
            Collection<Role> roleList = userService.getSetOfRoles(role);
            person.setRoles(roleList);
        }

        userService.add (person);
        return "redirect:/admin";
    }
}
