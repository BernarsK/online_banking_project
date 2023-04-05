package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        UUID userId = (UUID)session.getAttribute("UUID");
        // check if session exists
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        User logedInUser = userService.findUserById(userId);
        Integer userLevel = logedInUser.getUserLevel();
        model.addAttribute("userLevel", userLevel);
        model.addAttribute("userId", userId);

        return "home";
    }
}
