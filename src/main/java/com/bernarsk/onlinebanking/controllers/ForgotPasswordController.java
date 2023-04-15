package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot-password") public String showForgotPassword(Model model) {
        model.addAttribute("user", new User());
        return "forgot-password";
    }

    @PostMapping("/forgot-password") public String postForgotPassword(Model model, @RequestParam String email){
        if (forgotPasswordService.checkEmailExists(email)) {
            // logic here
            forgotPasswordService.sendRecoveryEmail(email);
            return "redirect:/login";
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("error", "This email address is not registered!");
        }
        return "forgot-password";
    }

}
