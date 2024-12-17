package com.don.bank.util.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenCookie {

    public static void addToken (HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public static void removeToken (HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
