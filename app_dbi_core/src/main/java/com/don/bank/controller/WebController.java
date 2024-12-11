package com.don.bank.controller;

import com.don.bank.dto.AccountDTO;
import com.don.bank.entity.Account;
import com.don.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);
    private final AccountService accountService;

    public WebController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currencies", Arrays.asList(Account.Currency.values()));
        model.addAttribute("page", "index");
        return "index";
    }

    @GetMapping("/auth")
    public String auth(Model model) {
        model.addAttribute("page", "auth");
        return "auth";
    }

    @GetMapping("/account/{id}")
    public String auth(@PathVariable Long id, Model model) {

        try{
            Account account = accountService.getAccountById(id);

            if (account == null) {
                model.addAttribute("error", "Account whith ID " + id + " not found");
                model.addAttribute("page", "error");
                return "error";
            }

            model.addAttribute("currencies", Arrays.asList(Account.Currency.values()));
            model.addAttribute("types", Arrays.asList(Account.Type.values()));
            model.addAttribute("account", account);
            model.addAttribute("page", "account");
            return "account";
        } catch (Exception e){
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("page", "error");
            return "error";
        }

    }

}
