package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.EmailService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    private
    @GetMapping("/home") String home(HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        UUID userId = (UUID)session.getAttribute("UUID");
        // check if session exists
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        User logedInUser = userService.findUserById(userId);
        Integer userLevel = logedInUser.getUserLevel();
        model.addAttribute("userLevel", userLevel);
        model.addAttribute("userId", userId);

        List<Account> accounts = accountService.getAllAccountsForUser(userId);
        model.addAttribute("accounts", accounts);
        model.addAttribute("user", logedInUser);

        return "home";
    }
}
