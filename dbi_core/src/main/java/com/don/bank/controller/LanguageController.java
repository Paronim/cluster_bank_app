package com.don.bank.controller;

import com.don.bank.util.languageUtils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lang")
public class LanguageController {

    private static final Logger log = LoggerFactory.getLogger(LanguageController.class);

    @PostMapping("/change")
    public void changeLang(@RequestBody String lang) {
        try {
            LanguageUtils.setLang(lang);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
