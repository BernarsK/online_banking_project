package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/my-account") String postMyAccount(HttpSession session, Model model, @RequestParam String password1, @RequestParam String password2, @RequestParam String password, @RequestParam String name, @RequestParam String surname, @RequestParam String email) {
        // find user
        UUID userID = (UUID) session.getAttribute("UUID");
        User logedInUser = userService.findUserById(userID);
        model.addAttribute("user", logedInUser);

        // user did not enter new password so he does not want to change it
        if (password1 == "" && password2 == "") {
            // no new passwords entered, update user name/surname/email
            // check if entered password is correct
            if (!passwordEncoder.matches(password, logedInUser.getPassword())) {
                model.addAttribute("error", "Your current password is not entered correctly!");
                return "my-account";
            } else {
                // update acc details
                logedInUser.setName(name);
                logedInUser.setSurname(surname);
                logedInUser.setEmail(email);
                userRepository.save(logedInUser);
                return "redirect:/home";
            }
        } else {
            // user wants to change password
            if (!password1.equals(password2)) {
                // entered passwords do not match
                model.addAttribute("error", "Entered passwords do not match!");
                return "my-account";
            } else {
                if (!passwordEncoder.matches(password, logedInUser.getPassword())) {
                    model.addAttribute("error", "Your current password is not entered correctly!");
                    return "my-account";
                } else {
                    // update acc details
                    logedInUser.setName(name);
                    logedInUser.setSurname(surname);
                    logedInUser.setEmail(email);
                    logedInUser.setPassword(passwordEncoder.encode(password1));
                    userRepository.save(logedInUser);
                    return "redirect:/home";
                }
            }
        }
    }
}
