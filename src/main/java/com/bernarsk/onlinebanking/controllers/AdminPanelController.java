package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.exceptions.MyAccountException;
import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private TransactionSendService transactionSendService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MyAccountService myAccountService;

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
    @GetMapping("/adminView-user")
    public String showUserFromEmail(@RequestParam("userEmail")String userEmail, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        User loggedInUser = userService.findUserById(userID);
        if (loggedInUser.getUserLevel()==1){//only admins can look for other user info
            User userInfo = userService.findUserByEmail(userEmail);
            if (userInfo==null){ //checks if there is any user with provided email
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
    @GetMapping("/adminView-unapprovedTransactions")
    public String showUserAccount(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        User loggedInUser = userService.findUserById(userID);
        if (loggedInUser.getUserLevel()==1){//only admins can have access
            List<Transaction> unapprovedTransactions = transactionViewService.findAllByStatusApproved(0);
            if (unapprovedTransactions.isEmpty()){ //checks if there is any unapproved transactions
                redirectAttributes.addFlashAttribute("error", "no unapproved transactions");
                return "redirect:/admin-panel";
            }
            model.addAttribute("unapprovedTransactions", unapprovedTransactions);
            return "adminView-unapprovedTransactions";
        }
        else {
            redirectAttributes.addFlashAttribute("error", "not logged in as admin");
            return "redirect:/login";
        }

    }
    private static final Logger logger = LoggerFactory.getLogger(AdminPanelController.class);

    @PostMapping("/change-transaction-status")
    public String approveTransaction(@RequestParam("transactionId") UUID transactionId, @RequestParam("statusValue") Integer statusValue, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        User loggedInUser = userService.findUserById(userID);
        if (loggedInUser.getUserLevel()==1){//only admins can have access
            Transaction penidngTransaction = transactionViewService.findByID(transactionId);
            if (penidngTransaction == null){ //checks if transaction was found (should always be true)
                redirectAttributes.addFlashAttribute("error", "error couldn't find transaction");
                return "redirect:/adminView-unapprovedTransactions";
            }

            try{
                transactionSendService.changeTransactionStatus(penidngTransaction, session, statusValue);
                if (statusValue==0){
                    logger.info("Transaction with ID: " + transactionId + " has been approved by admin: " + loggedInUser.getId());
                }
                else logger.info("Transaction with ID: " + transactionId + " has been canceled by admin: " + loggedInUser.getId());
            }
            catch (TransactionException e) {
                model.addAttribute("error", "transaction canceled, " + e.getMessage());
                logger.info("Transaction with ID: " + transactionId + " failed to be approved (not enough funds) by admin: " + loggedInUser.getId());
                redirectAttributes.addFlashAttribute("error", "transaction canceled, " + e.getMessage());
            }
            List<Transaction> unapprovedTransactions = transactionViewService.findAllByStatusApproved(0);
            if (unapprovedTransactions.isEmpty()){ //checks if there is any unapproved transactions
                return "redirect:/admin-panel";
            }
            model.addAttribute("unapprovedTransactions", unapprovedTransactions);
            return "adminView-unapprovedTransactions";
        }
        else {
            redirectAttributes.addFlashAttribute("error", "not logged in as admin");
            return "redirect:/login";
        }

    }
    @PostMapping("/admin-view-account-set-full-name") String changeNameSurname(@RequestParam("userEmail") String userEmail, HttpSession session, Model model, RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam String surname) {
        // find user
        User selectedUser = userService.findUserByEmail(userEmail);
        UUID userID = selectedUser.getId();
        String email=selectedUser.getEmail();

        try {
            myAccountService.setFullName(userID, name, surname, session);
            return "redirect:/adminView-user?userEmail="+email;
        }
        catch (MyAccountException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/adminView-user?userEmail="+email;
        }
    }
    @PostMapping("/admin-view-account-change-email") String changeEmail(@RequestParam("userEmail") String userEmail, HttpSession session, Model model, RedirectAttributes redirectAttributes, @RequestParam String email) {
        User selectedUser = userService.findUserByEmail(userEmail);
        UUID userID = selectedUser.getId();
        String currentEmail=selectedUser.getEmail();
        try {
            myAccountService.setEmail(userID, email, session);
            return "redirect:/adminView-user?userEmail="+email;
        }
        catch (MyAccountException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/adminView-user?userEmail="+currentEmail;
        }
    }
    @PostMapping("/admin-view-account-change-password") String changePassword(@RequestParam("userEmail") String userEmail, HttpSession session, Model model, RedirectAttributes redirectAttributes, @RequestParam String password1, @RequestParam String password2) {
        User selectedUser = userService.findUserByEmail(userEmail);
        UUID userID = selectedUser.getId();
        String email=selectedUser.getEmail();
        if (!password1.equals(password2)){
            redirectAttributes.addFlashAttribute("error", "passwords dont match");
            return "redirect:/adminView-user?userEmail="+email;
        }
        try {
            String password ="not needed for admins"; //old password not needed for admins so can pass any string
            myAccountService.changePassword(userID, password1, password, session);
            return "redirect:/adminView-user?userEmail="+email;
        }
        catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/adminView-user?userEmail="+email;
        }
    }


}
