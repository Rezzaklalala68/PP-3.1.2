package ru.kata.spring.boot_security.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;



@Controller
@RequestMapping("/user")
public class PeopleController {
    private final UserService userService;


    @Autowired
    public PeopleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserInfo(Model model) {
        model.addAttribute("person", userService
                .loadUserByUsername(userService.getCurrentUsername()));
        return "user";
    }
    @GetMapping("/user/{username}")
    public String getUser(@PathVariable String username, Model model) {
        model.addAttribute("person", userService.loadUserByUsername(username));
        return "user";
    }
}