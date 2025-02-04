package ru.kata.spring.boot_security.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;



@Controller
@RequestMapping("/users")
public class PeopleController {
    private final UserService userService;


    @Autowired
    public PeopleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUserInfo (Model model) {
        model.addAttribute("user", userService
                .loadUserByUsername(userService.getCurrentUsername()));
        return "add";
    }
}