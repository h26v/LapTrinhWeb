package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IAuthorService_24162046;
import vn.iotstar.service.IBookService_24162046;
import vn.iotstar.service.IRatingService_24162046;
import vn.iotstar.service.IUserService_24162046;
import vn.iotstar.service.impl.AuthorService_24162046;
import vn.iotstar.service.impl.BookService_24162046;
import vn.iotstar.service.impl.RatingService_24162046;
import vn.iotstar.service.impl.UserService_24162046;

import java.io.IOException;

@WebServlet(urlPatterns = {"/admin", "/admin/home"})
public class AdminHomeController_24162046 extends HttpServlet {

    private final IBookService_24162046 bookService = new BookService_24162046();
    private final IAuthorService_24162046 authorService = new AuthorService_24162046();
    private final IUserService_24162046 userService = new UserService_24162046();
    private final IRatingService_24162046 ratingService = new RatingService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("bookCount", bookService.count());
        req.setAttribute("authorCount", authorService.count());
        req.setAttribute("userCount", userService.count());
        req.setAttribute("reviewCount", ratingService.count());
        req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
    }
}
