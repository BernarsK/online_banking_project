package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.service.AccountService;
import com.bernarsk.onlinebanking.service.TransactionViewService;
import com.bernarsk.onlinebanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    public String showTransactionForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        List<Account> accounts = accountService.getAllAccountsForUser(userID);
        model.addAttribute("accounts", accounts);
        return "view-accounts";
    }
    @GetMapping("/view-account-transactions")
    public String processTransactionForm(@RequestParam("selectedAccount")String accountNumber,
                                         @RequestParam(name = "startDate", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(name = "endDate", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                         @RequestParam(name = "status", required = false) Integer status,
                                         @RequestParam(name = "reference", required = false) String reference,
                                         HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // call service class
        UUID userID = (UUID) session.getAttribute("UUID");
        // check if session exists
        if (userID == null) {
            redirectAttributes.addFlashAttribute("error", "not logged in");
            return "redirect:/login";
        }
        if (endDate == null) { //if no end date provided default value is now
            endDate = LocalDate.now();
        }
        Account account = accountService.findByAccountNumber(accountNumber);
        List<Transaction> accountTransactions = transactionViewService.viewAccountTransactions(session, account, account);//can change later to look for incoming or outgoing transactions, curently looks for all
        if (startDate != null) {
            accountTransactions = transactionViewService.filterByDate(accountTransactions, startDate, endDate);
        }
        if (status != null) {
            accountTransactions = transactionViewService.filterByStatus(accountTransactions, status);
        }
        if (reference != null) {
            accountTransactions = transactionViewService.filterByReference(accountTransactions, reference);
        }
        model.addAttribute("accountTransactions", accountTransactions);
        model.addAttribute("selectedAccount", accountNumber);
        return "view-account-transactions";
    }
}
