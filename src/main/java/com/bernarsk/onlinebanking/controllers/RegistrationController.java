package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class RegistrationController {
    @Autowired
    private RegistrationService userService;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("user") User user) {
        // call service class
        userService.saveUser(user);
        // change this later
        return "redirect:/registration-success";
    }
}
