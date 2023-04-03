package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired private UserRepository userRepository;

    // email existing check
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user) {
        // check if email exists
        String email = user.getEmail();
        if (isEmailExists(email)) {
            //
        } else {
            //
        }
        return "redirect:/login-success";
    }
}