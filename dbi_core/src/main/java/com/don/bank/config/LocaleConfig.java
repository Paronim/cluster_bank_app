package com.don.bank.config;

import com.don.bank.util.languageUtils.LanguageUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleConfig();
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String lang = Arrays.stream(request.getCookies())
                .filter(cookie -> "lang".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (lang == null) {
            lang = request.getHeader("Accept-Language").split(",")[0].split("-")[0];

            if (lang != null && !lang.isEmpty()) {

                LanguageUtils.setLang(lang);
            }
        }

        return lang == null || lang.isEmpty() ? Locale.getDefault() : Locale.forLanguageTag(lang);
    }
}
