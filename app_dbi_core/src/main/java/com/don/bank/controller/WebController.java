package com.don.bank.controller;

import com.don.bank.entity.Account;
import com.don.bank.service.AccountService;
import com.don.bank.service.WebService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);
    private final AccountService accountService;
    private final WebService webService;

    public WebController(AccountService accountService, WebService webService){
        this.webService = webService;
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currencies", Arrays.asList(Account.Currency.values()));
        model.addAttribute("page", "index");
        return "index";
    }

    @GetMapping("/login")
    public String auth(Model model, HttpServletRequest request) throws IOException {

        String token = webService.getToken(request);

        if(token != null) {
            return "redirect:/";
        }

        model.addAttribute("page", "login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) throws IOException {

        String token = webService.getToken(request);

        if(token != null) {
            return "redirect:/";
        }

        model.addAttribute("page", "register");
        return "register";
    }

    @GetMapping("/login/yandex")
    public String yandexAuth(Model model, HttpServletRequest request) throws IOException {

        String token = webService.getToken(request);

        if(token != null) {
            return "redirect:/";
        }

        return "yandexAuth";
    }

    @GetMapping("/account/{id}")
    public String account(@PathVariable Long id, Model model) {

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
