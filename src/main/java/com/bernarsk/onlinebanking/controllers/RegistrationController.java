package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class RegistrationController {
    @Autowired private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("user") User user) {
        // Check if email address is already in use
//        User existingUser = UserRepository.findByEmail(user.getEmail());
////        if (existingUser != null) {
////            model.addAttribute("errorMessage", "Email address is already in use");
////            return "register";
////        }

        // get account creation time
        user.setDateCreated(LocalDate.now());

        // Save user to database
        userRepository.save(user);
        // Save user to database or perform other registration logic
        return "redirect:/registration-success";
    }
}
