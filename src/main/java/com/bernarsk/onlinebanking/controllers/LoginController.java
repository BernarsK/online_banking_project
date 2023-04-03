package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // email existing check

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam String password, @RequestParam String email) {

        boolean authenticated = loginService.authenticateUser(email, password);
        if (authenticated) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }




//        // check if email exists
//        if (isEmailExists(email)) {
//            // if email is correct, check password
//            if (!user.getPassword().equals(password)) {
//                model.addAttribute("error", "Incorrect password");
//                return "login";
//            } else {
//                // add logic to log-in user (session)
//            }
//        } else {
//            // if email is not in database, return error
//            model.addAttribute("error", "Email address is not registered!");
//            return "login";
//        }
//        return "redirect:/login-success";
    }
}