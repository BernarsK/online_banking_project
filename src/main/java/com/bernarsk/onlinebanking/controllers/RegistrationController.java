package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.exceptions.RegistrationException;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@RequestParam String email, @RequestParam String password, Model model) {
        // call service class
        try {
            registrationService.saveUser(email, password);
            // redirect to log in
            return "redirect:/login";
        }
        catch (RegistrationException e) {
            model.addAttribute("user", new User());//needed because html file uses user object for values
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
