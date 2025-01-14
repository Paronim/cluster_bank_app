package com.don.bank.util.languageUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LanguageUtils {

    public static void setLang(String lang) {

        Cookie cookie = new Cookie("lang", lang);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletResponse response = attrs.getResponse();
            if (response != null) {
                response.addCookie(cookie);
            }
        }
    }
}
