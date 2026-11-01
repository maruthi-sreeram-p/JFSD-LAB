package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/datetime")
public class DateTimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Current Date and Time</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        out.println("<h1>Current Date and Time</h1>");
        out.println("<table>");
        out.println("<tr><th>Date</th><td>" + date + "</td></tr>");
        out.println("<tr><th>Time</th><td>" + time + "</td></tr>");
        out.println("</table>");
        out.println("<p class=\"muted\">Server time. Reload the page to see it change.</p>");
        out.println("</body></html>");
    }
}
