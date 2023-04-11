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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AddAccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("create-account")
    public String showAccountForm(Model model, HttpSession session) {
        // check if session exists
        UUID userSess = (UUID) session.getAttribute("UUID");
        if (userSess == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        model.addAttribute("account", new Account());
        return "create-account";
    }
    @PostMapping("/create-account")
    public String processRegistrationForm(@RequestParam String accountType, HttpSession session) {
        Integer accountTypeInt = 0;
        switch (accountType) {
            case "Checking":
                accountTypeInt = 0;
                break;
            case "Savings":
                accountTypeInt = 1;
                break;
            default:
                System.out.println("ERROR!");
        }
        UUID userSess = (UUID) session.getAttribute("UUID");
        accountService.makeNewAccount(userSess, accountTypeInt);

        return "redirect:/home";
    }

}
