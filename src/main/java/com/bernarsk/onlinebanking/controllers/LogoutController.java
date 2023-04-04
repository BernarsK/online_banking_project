package com.bernarsk.onlinebanking.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // destroy session, remove attributes
        session.removeAttribute("email");
        session.invalidate();

        return "redirect:/login";
    }
}
