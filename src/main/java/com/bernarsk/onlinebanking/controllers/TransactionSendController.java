package com.bernarsk.onlinebanking.controllers;

import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.service.TransactionSendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionSendController {
    @Autowired
    private TransactionSendService transactionSendService;
    @GetMapping("/new-transaction")
    public String showTransactionForm(HttpSession session, Model model) {
        String emailSess = (String) session.getAttribute("email");
        // check if session exists
        System.out.println(session);
        if (emailSess == null) {
            return "redirect:/login";
        }
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
