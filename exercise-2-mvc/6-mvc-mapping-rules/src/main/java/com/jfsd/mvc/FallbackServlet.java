package com.jfsd.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Mapped to "/" - the default servlet of this application. It receives every
 * URL that no other pattern matched.
 *
 * Mapping "/" replaces Tomcat's own default servlet, which normally serves
 * static files such as index.html and style.css. To keep those working, real
 * files are handed back to the container's servlet called "default".
 */
public class FallbackServlet extends MappingServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        boolean isFile = !path.endsWith("/") && getServletContext().getResource(path) != null;
        if (isFile) {
            RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("default");
            dispatcher.forward(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        show(request, response, getInitParameter("rule"));
    }
}
