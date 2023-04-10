package com.bernarsk.onlinebanking.controllers;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


@Controller
public class TransactionViewControler {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionViewService transactionViewService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/view-accounts")
    public String showTransactionForm(HttpSession session, Model model) {
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) {
            return "redirect:/login";
        }
        List<Account> accounts = accountService.getAllAccountsForUser(userID);
        model.addAttribute("accounts", accounts);
        return "view-accounts";
    }
    @PostMapping("/view-account-transactions")
    public String processTransactionForm(@RequestParam("selectedAccount")String accountNumber, HttpSession session, Model model) {
        // call service class
        Account account = accountService.findByAccountNumber(accountNumber);
        List<Transaction> accountTransactions = transactionViewService.viewAccountTransactions(session, account, account);//can change later to look for incoming or outgoing transactions, curently looks for all
        model.addAttribute("accountTransactions", accountTransactions);
        model.addAttribute("selectedAccount", accountNumber);
        return "view-account-transactions";
    }
}
