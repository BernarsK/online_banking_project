package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.exceptions.MyAccountException;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import com.bernarsk.onlinebanking.service.MyAccountService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class MyAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyAccountService myAccountService;


    @GetMapping("/my-account") String myAccount(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        // check if user is logged in
        UUID userID = (UUID) session.getAttribute("UUID");
        if (userID == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        User logedInUser = userService.findUserById(userID);
        model.addAttribute("user", logedInUser);
        return "my-account";
    }

    @PostMapping("/my-account-set-full-name") String changeNameSurname(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam String surname) {
        // find user
        UUID userID = (UUID) session.getAttribute("UUID");

        try {
            myAccountService.setFullName(userID, name, surname, session);
            return "redirect:/home";
        }
        catch (MyAccountException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/my-account";
        }
    }
    @PostMapping("/my-account-change-email") String changeEmail(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String email) {
        UUID userID = (UUID) session.getAttribute("UUID");

        try {
            myAccountService.setEmail(userID, email, session);
            return "redirect:/home";
        }
        catch (MyAccountException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/my-account";
        }
    }
    @PostMapping("/my-account-change-password") String postMyAccount(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String password1, @RequestParam String password2, @RequestParam String password) {
        UUID userID = (UUID) session.getAttribute("UUID");
        if (!password1.equals(password2)){
            redirectAttributes.addFlashAttribute("error", "passwords dont match");
            return "redirect:/my-account";
        }
        try {
            myAccountService.changePassword(userID, password1, password, session);
            return "redirect:/home";
        }
        catch (MyAccountException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/my-account";
        }
    }
}
