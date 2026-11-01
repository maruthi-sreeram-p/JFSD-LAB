package com.jfsd.mvc.controller;

import com.jfsd.mvc.model.StudentStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller of the application. It is not annotated: web.xml registers it
 * and maps it to /students (see WEB-INF/web.xml).
 */
public class StudentController extends HttpServlet {

    private final StudentStore store = new StudentStore();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("students", store.all());
        request.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String name = request.getParameter("name").trim();
            String email = request.getParameter("email").trim();
            String course = request.getParameter("course");

            if (store.emailExists(email)) {
                request.setAttribute("error", "A student with this email is already registered");
                request.setAttribute("students", store.all());
                request.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(request, response);
                return;
            }
            store.add(name, email, course);
        } else if ("remove".equals(action)) {
            store.remove(Integer.parseInt(request.getParameter("id")));
        }
        response.sendRedirect(request.getContextPath() + "/students");
    }
}
