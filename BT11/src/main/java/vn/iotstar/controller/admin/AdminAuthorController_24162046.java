package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Author_24162046;
import vn.iotstar.service.IAuthorService_24162046;
import vn.iotstar.service.impl.AuthorService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Cau 6: CRUD Authors co phan trang.
 * GET  /admin/authors            -> danh sach (phan trang)
 * GET  /admin/authors/add        -> form them
 * POST /admin/authors/add        -> luu them
 * GET  /admin/authors/edit?id=   -> form sua
 * POST /admin/authors/edit       -> luu sua
 * POST /admin/authors/delete     -> xoa
 */
@WebServlet(urlPatterns = "/admin/authors/*")
public class AdminAuthorController_24162046 extends HttpServlet {

    private final IAuthorService_24162046 authorService = new AuthorService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = action(req);
        switch (action) {
            case "/add" -> showForm(req, resp, new Author_24162046());
            case "/edit" -> {
                Author_24162046 author = authorService.findById(ParamUtil_24162046.parseInt(req.getParameter("id"), -1));
                if (author == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/authors");
                    return;
                }
                showForm(req, resp, author);
            }
            default -> {
                int page = ParamUtil_24162046.parseInt(req.getParameter("page"), 1);
                req.setAttribute("result", authorService.getPage(page, Constant_24162046.ADMIN_PAGE_SIZE));
                req.setAttribute("pageUrl", req.getContextPath() + "/admin/authors?page=");
                req.getRequestDispatcher("/views/admin/author-list.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = action(req);
        if ("/delete".equals(action)) {
            authorService.delete(ParamUtil_24162046.parseInt(req.getParameter("id"), -1));
            req.getSession().setAttribute("flash", "Đã xóa tác giả");
            resp.sendRedirect(req.getContextPath() + "/admin/authors?page=" + ParamUtil_24162046.parseInt(req.getParameter("page"), 1));
            return;
        }

        boolean isEdit = "/edit".equals(action);
        Author_24162046 author;
        if (isEdit) {
            author = authorService.findById(ParamUtil_24162046.parseInt(req.getParameter("authorId"), -1));
            if (author == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/authors");
                return;
            }
        } else {
            author = new Author_24162046();
        }

        String name = ParamUtil_24162046.trim(req.getParameter("authorName"));
        author.setAuthorName(name);
        String error = null;
        if (ParamUtil_24162046.isBlank(name)) {
            error = "Tên tác giả không được để trống";
        } else if (name.length() > 100) {
            error = "Tên tác giả tối đa 100 ký tự";
        }
        String dob = req.getParameter("dateOfBirth");
        try {
            author.setDateOfBirth(ParamUtil_24162046.isBlank(dob) ? null : LocalDate.parse(dob.trim()));
        } catch (RuntimeException e) {
            error = "Ngày sinh không hợp lệ";
        }

        if (error != null) {
            req.setAttribute("error", error);
            showForm(req, resp, author);
            return;
        }

        if (isEdit) {
            authorService.update(author);
            req.getSession().setAttribute("flash", "Đã cập nhật tác giả");
        } else {
            authorService.create(author);
            req.getSession().setAttribute("flash", "Đã thêm tác giả mới");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Author_24162046 author)
            throws ServletException, IOException {
        req.setAttribute("author", author);
        req.getRequestDispatcher("/views/admin/author-form.jsp").forward(req, resp);
    }

    private String action(HttpServletRequest req) {
        String path = req.getPathInfo();
        return path == null ? "/" : path;
    }
}
