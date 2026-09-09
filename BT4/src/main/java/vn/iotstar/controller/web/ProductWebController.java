package vn.iotstar.controller.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.ValidationUtil;

@WebServlet(urlPatterns = { "/product", "/product/detail" })
public class ProductWebController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getRequestURI().contains("detail")) {
            detail(req, resp);
        } else {
            list(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int total = productService.countActive();
        int totalPages = total == 0 ? 1 : (int) Math.ceil(total / (double) PAGE_SIZE);
        int page = parsePage(req.getParameter("page"));
        if (page > totalPages) {
            page = totalPages;
        }

        req.setAttribute("products", productService.findActive(page, PAGE_SIZE));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/views/web/product-list.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = ValidationUtil.parseId(req.getParameter("id"));
        if (id <= 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Product product = productService.findById(id);
        if (product == null || product.getStatus() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("product", product);
        req.getRequestDispatcher("/views/web/product-detail.jsp").forward(req, resp);
    }

    private int parsePage(String value) {
        int page = ValidationUtil.parseId(value);
        return page < 1 ? 1 : page;
    }
}
