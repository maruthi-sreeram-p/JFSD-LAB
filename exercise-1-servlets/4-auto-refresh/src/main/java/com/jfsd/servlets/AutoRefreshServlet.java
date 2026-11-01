package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sends a "Refresh" header so the browser reloads the page by itself.
 */
@WebServlet("/refresh")
public class AutoRefreshServlet extends HttpServlet {

    private static final int DEFAULT_SECONDS = 5;

    private final AtomicInteger loads = new AtomicInteger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int seconds = readSeconds(request.getParameter("seconds"));

        // this one header is the whole trick: reload this page every N seconds
        response.setHeader("Refresh", String.valueOf(seconds));

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Auto Refresh</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        out.println("<h1>Auto refresh</h1>");
        out.println("<p>This page reloads itself every <b>" + seconds + "</b> seconds.</p>");
        out.println("<table>");
        out.println("<tr><th>Server time</th><td>" + time + "</td></tr>");
        out.println("<tr><th>Times loaded</th><td>" + loads.incrementAndGet() + "</td></tr>");
        out.println("</table>");
        out.println("<p class=\"muted\">Change the interval: ");
        out.println("<a href=\"refresh?seconds=2\">2s</a> | ");
        out.println("<a href=\"refresh?seconds=5\">5s</a> | ");
        out.println("<a href=\"refresh?seconds=10\">10s</a></p>");
        out.println("</body></html>");
    }

    private int readSeconds(String value) {
        if (value == null) {
            return DEFAULT_SECONDS;
        }
        try {
            int n = Integer.parseInt(value.trim());
            return (n >= 1 && n <= 60) ? n : DEFAULT_SECONDS;
        } catch (NumberFormatException e) {
            return DEFAULT_SECONDS;
        }
    }
}
