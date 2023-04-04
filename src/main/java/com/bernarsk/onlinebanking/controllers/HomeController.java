package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // check if session exists
        if (session.getAttribute("email") == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", new User());
        return "home";
    }
}
