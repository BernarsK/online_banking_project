package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    private
    @GetMapping("/home") String home(HttpSession session, Model model) {
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

        List<Account> accounts = accountService.getAllAccountsForUser(userId);
        System.out.println(accounts);
        model.addAttribute("accounts", accounts);

        return "home";
    }
}
