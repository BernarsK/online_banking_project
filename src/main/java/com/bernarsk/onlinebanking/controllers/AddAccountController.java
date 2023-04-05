package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.utils.IbanGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddAccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("create-account")
    public String showAccountForm(Model model, HttpSession session) {
        // check if session exists
        String emailSess = (String) session.getAttribute("email");
        if (emailSess == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        return "create-account";
    }
    @PostMapping("/create-account")
    public String processRegistrationForm(@ModelAttribute("account") Account account) {
        // call service class
        IbanGenerator.generateIban();
        // change this later
        return "redirect:/home";
    }

}
