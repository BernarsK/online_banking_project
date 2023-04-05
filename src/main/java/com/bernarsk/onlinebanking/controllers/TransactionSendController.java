package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.TransactionSendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class TransactionSendController {
    @Autowired
    private TransactionSendService transactionSendService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/new-transaction")
    public String showTransactionForm(HttpSession session, Model model) {
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) {
            return "redirect:/login";
        }
        List<Account> accounts = accountService.getAllAccountsForUser(userID);
        model.addAttribute("userAccounts", accounts);
        model.addAttribute("transaction", new Transaction());
        return "new-transaction";
    }
    @PostMapping("/process-transaction")
    public String processTransactionForm(@ModelAttribute("transaction") Transaction transaction, HttpSession session) {
        // call service class
        transactionSendService.saveTransaction(session, transaction);
        // change this later
        return "redirect:/transaction-sent";
    }
}
