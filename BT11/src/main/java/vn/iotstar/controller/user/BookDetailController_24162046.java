package vn.iotstar.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Book_24162046;
import vn.iotstar.service.IBookService_24162046;
import vn.iotstar.service.IRatingService_24162046;
import vn.iotstar.service.impl.BookService_24162046;
import vn.iotstar.service.impl.RatingService_24162046;
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;

/**
 * Cau 4: Trang chi tiet 1 cuon sach + danh sach review + form them review.
 */
@WebServlet(urlPatterns = "/book/detail")
public class BookDetailController_24162046 extends HttpServlet {

    private final IBookService_24162046 bookService = new BookService_24162046();
    private final IRatingService_24162046 ratingService = new RatingService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = ParamUtil_24162046.parseInt(req.getParameter("id"), -1);
        Book_24162046 book = bookService.findById(id);
        if (book == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sách");
            return;
        }
        req.setAttribute("book", book);
        req.setAttribute("reviews", ratingService.findByBook(id));
        req.getRequestDispatcher("/views/user/book-detail.jsp").forward(req, resp);
    }
}
