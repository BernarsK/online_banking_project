package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.TransactionViewService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller

public class AdminPanelController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionViewService transactionViewService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/admin-panel")
    public String showAdminPanel(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) { //checks if user is logged in
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        User loggedInUser = userService.findUserById(userID);
        if (loggedInUser.getUserLevel()!=1) { //checks if logged in user is admin
            redirectAttributes.addFlashAttribute("error", "not logged in as admin");
            return "redirect:/login";
        }
        return "admin-panel";
    }
    @PostMapping("/adminView-user")
    public String showUserFromEmail(@RequestParam("userEmail")String userEmail, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        User loggedInUser = userService.findUserById(userID);
        if (loggedInUser.getUserLevel()==1){//only admins can look for other user info
            User userInfo = userService.findUserByEmail(userEmail);
            if (userInfo==null){ //checks if there is anyuser with provided email
                redirectAttributes.addFlashAttribute("error", "no user with that email");
                return "redirect:/admin-panel";
            }
            model.addAttribute("user", userInfo);
            List<Account> accounts = accountService.getAllAccountsForUser(userInfo.getId());
            model.addAttribute("accounts", accounts);
            return "adminView-user";
        }
        else {
            redirectAttributes.addFlashAttribute("error", "not logged in as admin");
            return "redirect:/login";
        }

    }

}
