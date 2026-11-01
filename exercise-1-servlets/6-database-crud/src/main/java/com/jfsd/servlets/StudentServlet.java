package com.jfsd.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * One servlet for the whole insert / update / delete cycle.
 *   GET  /students            -> list of records + add form
 *   GET  /students?edit=3     -> same page, form filled with record 3
 *   POST /students (action)   -> insert, update or delete, then redirect to the list
 */
@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private final StudentDao dao = new StudentDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Student> students = dao.findAll();

            Student editing = null;
            String edit = request.getParameter("edit");
            if (edit != null) {
                editing = dao.findById(parseId(edit));
            }
            showPage(response, students, editing, request.getParameter("msg"));
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("insert".equals(action)) {
                dao.insert(readStudent(request));
                response.sendRedirect("students?msg=added");
            } else if ("update".equals(action)) {
                Student s = readStudent(request);
                s.setId(parseId(request.getParameter("id")));
                dao.update(s);
                response.sendRedirect("students?msg=updated");
            } else if ("delete".equals(action)) {
                dao.delete(parseId(request.getParameter("id")));
                response.sendRedirect("students?msg=deleted");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private Student readStudent(HttpServletRequest request) {
        Student s = new Student();
        s.setName(request.getParameter("name").trim());
        s.setEmail(request.getParameter("email").trim());
        s.setCourse(request.getParameter("course").trim());
        return s;
    }

    private int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showPage(HttpServletResponse response, List<Student> students,
                          Student editing, String msg) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Student Records</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head><body>");
        out.println("<h1>Student records</h1>");

        String message = messageFor(msg);
        if (message != null) {
            out.println("<p><b>" + message + "</b></p>");
        }

        // the same form is used for insert and for update
        boolean updating = editing != null;
        out.println("<h2>" + (updating ? "Update student" : "Add student") + "</h2>");
        out.println("<form action=\"students\" method=\"post\">");
        out.println("<input type=\"hidden\" name=\"action\" value=\"" + (updating ? "update" : "insert") + "\">");
        if (updating) {
            out.println("<input type=\"hidden\" name=\"id\" value=\"" + editing.getId() + "\">");
        }
        out.println("<label>Name <input type=\"text\" name=\"name\" required value=\""
                + (updating ? escape(editing.getName()) : "") + "\"></label>");
        out.println("<label>Email <input type=\"email\" name=\"email\" required value=\""
                + (updating ? escape(editing.getEmail()) : "") + "\"></label>");
        out.println("<label>Course <input type=\"text\" name=\"course\" required value=\""
                + (updating ? escape(editing.getCourse()) : "") + "\"></label>");
        out.println("<p><button type=\"submit\">" + (updating ? "Update" : "Add") + "</button>");
        if (updating) {
            out.println(" <a href=\"students\">Cancel</a>");
        }
        out.println("</p></form>");

        out.println("<h2>All students</h2>");
        if (students.isEmpty()) {
            out.println("<p class=\"muted\">No records yet.</p>");
        } else {
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Course</th><th></th></tr>");
            for (Student s : students) {
                out.println("<tr>");
                out.println("<td>" + s.getId() + "</td>");
                out.println("<td>" + escape(s.getName()) + "</td>");
                out.println("<td>" + escape(s.getEmail()) + "</td>");
                out.println("<td>" + escape(s.getCourse()) + "</td>");
                out.println("<td><a href=\"students?edit=" + s.getId() + "\">Edit</a> "
                        + "<form action=\"students\" method=\"post\" style=\"display:inline\" "
                        + "onsubmit=\"return confirm('Delete this record?')\">"
                        + "<input type=\"hidden\" name=\"action\" value=\"delete\">"
                        + "<input type=\"hidden\" name=\"id\" value=\"" + s.getId() + "\">"
                        + "<button type=\"submit\">Delete</button></form></td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
        out.println("</body></html>");
    }

    private String messageFor(String code) {
        if ("added".equals(code)) {
            return "Student added.";
        } else if ("updated".equals(code)) {
            return "Student updated.";
        } else if ("deleted".equals(code)) {
            return "Student deleted.";
        }
        return null;
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
