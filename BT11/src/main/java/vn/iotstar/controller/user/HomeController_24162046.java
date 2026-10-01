package vn.iotstar.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IBookService_24162046;
import vn.iotstar.service.impl.BookService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;

/**
 * Cau 3: Trang chu hien thi tat ca sach, phan trang 6 sach / trang.
 * /home = Trang chu, /books = menu San pham (cung danh sach sach).
 */
@WebServlet(urlPatterns = {"/home", "/books"})
public class HomeController_24162046 extends HttpServlet {

    private final IBookService_24162046 bookService = new BookService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = ParamUtil_24162046.parseInt(req.getParameter("page"), 1);
        req.setAttribute("result", bookService.getPage(page, Constant_24162046.HOME_PAGE_SIZE));
        req.setAttribute("pageUrl", req.getContextPath() + req.getServletPath() + "?page=");
        req.setAttribute("isProductPage", "/books".equals(req.getServletPath()));
        req.getRequestDispatcher("/views/user/home.jsp").forward(req, resp);
    }
}
