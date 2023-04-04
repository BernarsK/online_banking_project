package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String emailSess = (String) session.getAttribute("email");
        // check if session exists
        if (emailSess == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        Integer userLevel = userService.findUserLevelByEmail(emailSess);
        System.out.println(userLevel);
        model.addAttribute("userLevel", userLevel);

        return "home";
    }
}
