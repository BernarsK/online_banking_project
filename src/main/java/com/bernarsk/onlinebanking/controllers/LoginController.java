package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login(Model model, @RequestParam String password, @RequestParam String email) {
        User user = userRepository.findByEmail(email);
        // check if email exists
        if (isEmailExists(email)) {
            // if email is correct, check password
            if (!user.getPassword().equals(password)) {
                model.addAttribute("error", "Incorrect password");
                return "login";
            }
        } else {
            // if email is not in database, return error
            model.addAttribute("error", "Email address is not registered!");
            return "login";
        }
        return "redirect:/login-success";
    }
}