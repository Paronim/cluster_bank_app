package com.don.bank.controller;

import com.don.bank.dto.AccountDTO;
import com.don.bank.entity.Account;
import com.don.bank.service.AccountService;
import com.don.bank.service.WebService;
import com.don.bank.util.JWT.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    private final AccountService accountService;

    private final WebService webService;

    private final JwtUtils jwtUtils;

    public WebController(AccountService accountService, WebService webService, JwtUtils jwtUtils){
        this.webService = webService;
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
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
    public String account(@PathVariable Long id, Model model, HttpServletRequest request) {

        try{

            String token = webService.getToken(request);
            long clientId = Long.parseLong(jwtUtils.extractUsername(token));

            List<AccountDTO> accounts = accountService.getAccountsByClientId(clientId);

            boolean exists = accounts.stream().anyMatch(a -> id == a.getId());

            if(exists){
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
            }

            return "redirect:/";
        } catch (Exception e){
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("page", "error");
            return "error";
        }

    }

}
