package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Reads the cookie sent by the browser and shows it. POST removes it.
 */
@WebServlet("/show-cookies")
public class ShowCookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String language = null;
        Cookie[] cookies = request.getCookies();      // null when the browser sent none
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("favLanguage")) {
                    language = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                }
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Cookie Value</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        out.println("<h1>Cookie value</h1>");

        if (language == null) {
            out.println("<p>No <code>favLanguage</code> cookie found.</p>");
        } else {
            out.println("<p>The browser sent the cookie <code>favLanguage</code> with the value <b>"
                    + escape(language) + "</b>.</p>");
            out.println("<form action=\"show-cookies\" method=\"post\">");
            out.println("<button type=\"submit\">Delete cookie</button></form>");
        }
        out.println("<p><a href=\"index.html\">Choose again</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie cookie = new Cookie("favLanguage", "");
        cookie.setMaxAge(0);                          // 0 tells the browser to remove it
        cookie.setPath(request.getContextPath() + "/");
        response.addCookie(cookie);
        response.sendRedirect("show-cookies");
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
