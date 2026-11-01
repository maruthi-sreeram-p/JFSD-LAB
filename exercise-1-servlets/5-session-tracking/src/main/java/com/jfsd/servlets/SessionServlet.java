package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Session tracking with HttpSession: the container gives every browser a
 * session id (JSESSIONID cookie) and keeps our attributes on the server.
 */
@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        boolean firstVisit = session.isNew();

        Integer visits = (Integer) session.getAttribute("visits");
        visits = (visits == null) ? 1 : visits + 1;
        session.setAttribute("visits", visits);

        String username = (String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Session Tracking</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        out.println("<h1>Session tracking</h1>");

        if (username == null) {
            out.println("<p>Welcome, new visitor.</p>");
        } else {
            out.println("<p>Welcome back, <b>" + escape(username) + "</b>.</p>");
        }

        out.println("<table>");
        out.println("<tr><th>Session id</th><td>" + session.getId() + "</td></tr>");
        out.println("<tr><th>New session?</th><td>" + (firstVisit ? "yes" : "no") + "</td></tr>");
        out.println("<tr><th>Created</th><td>" + new Date(session.getCreationTime()) + "</td></tr>");
        out.println("<tr><th>Last accessed</th><td>" + new Date(session.getLastAccessedTime()) + "</td></tr>");
        out.println("<tr><th>Timeout</th><td>" + session.getMaxInactiveInterval() + " seconds</td></tr>");
        out.println("<tr><th>Visits in this session</th><td>" + visits + "</td></tr>");
        out.println("</table>");

        out.println("<h2>Store a value in the session</h2>");
        out.println("<form action=\"session\" method=\"post\">");
        out.println("<input type=\"hidden\" name=\"action\" value=\"save\">");
        out.println("<input type=\"text\" name=\"username\" placeholder=\"Your name\" required> ");
        out.println("<button type=\"submit\">Save</button>");
        out.println("</form>");

        out.println("<form action=\"session\" method=\"post\">");
        out.println("<input type=\"hidden\" name=\"action\" value=\"end\">");
        out.println("<p><button type=\"submit\">End session</button> ");
        out.println("<a href=\"session\">Reload</a></p>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            String username = request.getParameter("username");
            if (username != null && !username.isBlank()) {
                request.getSession(true).setAttribute("username", username.trim());
            }
        } else if ("end".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        response.sendRedirect("session");
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
