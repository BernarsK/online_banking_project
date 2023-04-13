package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import com.bernarsk.onlinebanking.service.LoginService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // email existing check

        @GetMapping("/login")
        public String login(Model model) {
            model.addAttribute("user", new User());
            return "login";
        }

        @PostMapping("/login")
        public String login(@ModelAttribute("user") User user, Model model, @RequestParam String password, @RequestParam(defaultValue = "") String verificationCode, @RequestParam String email, HttpSession session) {

        Boolean authenticated = loginService.authenticateUser(session, email, password);
        User currentUser = userService.findUserByEmail(email);
        // check if user is not active
        if (authenticated) {
            if (currentUser.getActive() == 0 && verificationCode.isEmpty()) {
                // give error if user is not activated and nothing is entered in verification part
                model.addAttribute("error", "Your email is not activated yet!");
                model.addAttribute("isActivationCodeInvalid", true);
                return "login";
            } else if (currentUser.getActive() == 0 && verificationCode.length() > 0) {
                // check verification code if it is entered
                if (currentUser.getVerificationCode().matches(verificationCode)) {
                    // if the code is valid, activate user and reset verification code
                    currentUser.setActive(1);
                    currentUser.setVerificationCode(null);
                    userRepository.save(currentUser);
                    return "redirect:/home";
                } else {
                    // codes does not match each other
                    model.addAttribute("error", "The activation code entered is not valid!");
                    return "login";
                }
            } else
                return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid username or password!");
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