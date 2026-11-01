package com.jfsd.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * One class registered several times in web.xml, each time with a different
 * url-pattern. It prints which mapping the container chose for the request.
 */
public class MappingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        show(request, response, getInitParameter("rule"));
    }

    protected void show(HttpServletRequest request, HttpServletResponse response, String rule)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Mapping rules</title>");
        out.println("<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/style.css\"></head><body>");
        out.println("<h1>Handled by <code>" + getServletName() + "</code></h1>");
        out.println("<p>Rule: " + rule + "</p>");
        out.println("<table>");
        out.println("<tr><th>url-pattern in web.xml</th><td>" + getInitParameter("pattern") + "</td></tr>");
        out.println("<tr><th>requestURI</th><td>" + escape(request.getRequestURI()) + "</td></tr>");
        out.println("<tr><th>contextPath</th><td>" + escape(request.getContextPath()) + "</td></tr>");
        out.println("<tr><th>servletPath</th><td>" + escape(request.getServletPath()) + "</td></tr>");
        out.println("<tr><th>pathInfo</th><td>" + escape(String.valueOf(request.getPathInfo())) + "</td></tr>");
        out.println("</table>");
        out.println("<p><a href=\"" + request.getContextPath() + "/index.html\">Back to the test links</a></p>");
        out.println("</body></html>");
    }

    protected static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
