package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Takes the value chosen in the drop down and stores it in a cookie.
 */
@WebServlet("/add-cookie")
public class AddCookieServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String language = request.getParameter("language");
        if (language == null || language.isBlank()) {
            response.sendRedirect("index.html");
            return;
        }

        // cookie values can't hold spaces or ';' so encode the text first
        String value = URLEncoder.encode(language, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie("favLanguage", value);
        cookie.setMaxAge(60 * 60 * 24);                 // keep for one day
        cookie.setPath(request.getContextPath() + "/");
        response.addCookie(cookie);

        response.sendRedirect("show-cookies");
    }
}
