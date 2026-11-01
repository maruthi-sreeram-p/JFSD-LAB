package com.jfsd.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The controller. Every request to /tasks lands here and the controller:
 *   1. reads what the user asked for (action + parameters)
 *   2. calls the model (TaskRepository)
 *   3. chooses the view - forward to a JSP, or redirect to another URL
 *
 *   GET  /tasks                  list the tasks
 *   GET  /tasks?action=new       empty form
 *   GET  /tasks?action=edit&id=2 form filled with a task
 *   POST /tasks  action=save | toggle | delete
 */
@WebServlet("/tasks")
public class TaskController extends HttpServlet {

    private static final String LIST_VIEW = "/WEB-INF/views/list.jsp";
    private static final String FORM_VIEW = "/WEB-INF/views/form.jsp";

    private final TaskRepository repository = new TaskRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                request.setAttribute("tasks", repository.findAll());
                request.getRequestDispatcher(LIST_VIEW).forward(request, response);
                break;

            case "new":
                request.setAttribute("task", new Task());
                request.getRequestDispatcher(FORM_VIEW).forward(request, response);
                break;

            case "edit":
                Task task = repository.findById(readId(request));
                if (task == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No such task");
                    return;
                }
                request.setAttribute("task", task);
                request.getRequestDispatcher(FORM_VIEW).forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                String title = request.getParameter("title");
                Task task = new Task();
                task.setId(readId(request));
                task.setTitle(title == null ? "" : title.trim());

                if (task.getTitle().isEmpty()) {
                    // validation failed: show the form again with a message
                    request.setAttribute("task", task);
                    request.setAttribute("error", "Title cannot be empty");
                    request.getRequestDispatcher(FORM_VIEW).forward(request, response);
                    return;
                }
                Task existing = repository.findById(task.getId());
                if (existing != null) {
                    task.setDone(existing.isDone());
                }
                repository.save(task);
                break;

            case "toggle":
                Task t = repository.findById(readId(request));
                if (t != null) {
                    t.setDone(!t.isDone());
                }
                break;

            case "delete":
                repository.delete(readId(request));
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
                return;
        }

        // redirect after a change so a browser refresh does not repeat the POST
        response.sendRedirect(request.getContextPath() + "/tasks");
    }

    private int readId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
