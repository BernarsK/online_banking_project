package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.TransactionSendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class TransactionSendController {
    @Autowired
    private TransactionSendService transactionSendService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/new-transaction")
    public String showTransactionForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        List<Account> accounts = accountService.getAllAccountsForUser(userID);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", new Transaction());
        return "new-transaction";
    }
    @PostMapping("/new-transaction")
    public String processTransactionForm(@ModelAttribute("transaction") Transaction transaction, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // call service class
        UUID userID = (UUID) session.getAttribute("UUID");
        if (userID == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        try{
            transactionSendService.saveTransaction(session, transaction);
            // change this later
            return "redirect:/view-accounts";
        }
        catch (TransactionException e) {
            List<Account> accounts = accountService.getAllAccountsForUser(userID);
            model.addAttribute("accounts", accounts);//on error input fields are not cleared
            model.addAttribute("error", e.getMessage());
            return "new-transaction";
        }

    }
}
