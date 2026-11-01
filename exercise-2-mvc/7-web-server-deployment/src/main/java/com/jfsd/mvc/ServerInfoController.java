package com.jfsd.mvc;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/info")
public class ServerInfoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();

        ServerInfo info = new ServerInfo(
                context.getServerInfo(),
                context.getMajorVersion() + "." + context.getMinorVersion(),
                context.getContextPath(),
                context.getRealPath("/"));

        request.setAttribute("info", info);
        request.getRequestDispatcher("/WEB-INF/views/info.jsp").forward(request, response);
    }
}
