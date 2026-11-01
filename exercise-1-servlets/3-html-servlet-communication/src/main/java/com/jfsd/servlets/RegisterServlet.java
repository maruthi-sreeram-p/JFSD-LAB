package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // GET: values arrive in the query string (visible in the URL)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        PrintWriter out = startPage(response, "Hello");
        out.println("<h1>Hello, " + escape(name) + "!</h1>");
        out.println("<p class=\"muted\">Data received with GET: " + escape(request.getQueryString()) + "</p>");
        endPage(out);
    }

    // POST: values arrive in the request body
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String course = request.getParameter("course");
        String[] interests = request.getParameterValues("interest");

        String interestText = (interests == null) ? "none selected" : String.join(", ", interests);

        PrintWriter out = startPage(response, "Registration Details");
        out.println("<h1>Registration received</h1>");
        out.println("<table>");
        out.println("<tr><th>Name</th><td>" + escape(name) + "</td></tr>");
        out.println("<tr><th>Email</th><td>" + escape(email) + "</td></tr>");
        out.println("<tr><th>Gender</th><td>" + escape(gender) + "</td></tr>");
        out.println("<tr><th>Course</th><td>" + escape(course) + "</td></tr>");
        out.println("<tr><th>Interests</th><td>" + escape(interestText) + "</td></tr>");
        out.println("</table>");
        endPage(out);
    }

    private PrintWriter startPage(HttpServletResponse response, String title) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>" + title + "</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        return out;
    }

    private void endPage(PrintWriter out) {
        out.println("<p><a href=\"index.html\">Back to form</a></p>");
        out.println("</body></html>");
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
