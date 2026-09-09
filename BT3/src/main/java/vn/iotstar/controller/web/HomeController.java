package vn.iotstar.controller.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = { "/home" })
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("products", productService.findNewestActive(10));
        req.getRequestDispatcher("/views/web/home.jsp").forward(req, resp);
    }
}
