package com.jfsd.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Only here to feed the view. It puts data in the request and forwards to the
 * JSP; all the HTML lives in WEB-INF/views/products.jsp.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = List.of(
                new Product(1, "Wireless Mouse", 599, 25),
                new Product(2, "Mechanical Keyboard", 3499, 4),
                new Product(3, "USB-C Hub", 1299, 0),
                new Product(4, "Laptop Stand", 899, 12),
                new Product(5, "<b>Webcam</b> 1080p", 2199, 7));

        request.setAttribute("pageTitle", "Product catalogue");
        request.setAttribute("products", products);

        // JSPs under WEB-INF cannot be opened directly, only through a controller
        request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
    }
}
