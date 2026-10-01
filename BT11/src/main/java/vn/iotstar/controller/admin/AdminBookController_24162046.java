package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Book_24162046;
import vn.iotstar.service.IAuthorService_24162046;
import vn.iotstar.service.IBookService_24162046;
import vn.iotstar.service.impl.AuthorService_24162046;
import vn.iotstar.service.impl.BookService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cau 6: CRUD Books co phan trang.
 * GET  /admin/books            -> danh sach (phan trang)
 * GET  /admin/books/add        -> form them
 * POST /admin/books/add        -> luu them
 * GET  /admin/books/edit?id=   -> form sua
 * POST /admin/books/edit       -> luu sua
 * POST /admin/books/delete     -> xoa
 */
@WebServlet(urlPatterns = "/admin/books/*")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class AdminBookController_24162046 extends HttpServlet {

    private final IBookService_24162046 bookService = new BookService_24162046();
    private final IAuthorService_24162046 authorService = new AuthorService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = action(req);
        switch (action) {
            case "/add" -> showForm(req, resp, new Book_24162046());
            case "/edit" -> {
                Book_24162046 book = bookService.findById(ParamUtil_24162046.parseInt(req.getParameter("id"), -1));
                if (book == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/books");
                    return;
                }
                showForm(req, resp, book);
            }
            default -> {
                int page = ParamUtil_24162046.parseInt(req.getParameter("page"), 1);
                req.setAttribute("result", bookService.getPage(page, Constant_24162046.ADMIN_PAGE_SIZE));
                req.setAttribute("pageUrl", req.getContextPath() + "/admin/books?page=");
                req.getRequestDispatcher("/views/admin/book-list.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = action(req);
        if ("/delete".equals(action)) {
            bookService.delete(ParamUtil_24162046.parseInt(req.getParameter("id"), -1));
            req.getSession().setAttribute("flash", "Đã xóa sách");
            resp.sendRedirect(req.getContextPath() + "/admin/books?page=" + ParamUtil_24162046.parseInt(req.getParameter("page"), 1));
            return;
        }

        boolean isEdit = "/edit".equals(action);
        Book_24162046 book;
        if (isEdit) {
            book = bookService.findById(ParamUtil_24162046.parseInt(req.getParameter("bookid"), -1));
            if (book == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/books");
                return;
            }
        } else {
            book = new Book_24162046();
        }

        List<Integer> authorIds = new ArrayList<>();
        String[] ids = req.getParameterValues("authorIds");
        if (ids != null) {
            for (String id : ids) {
                int value = ParamUtil_24162046.parseInt(id, -1);
                if (value > 0) {
                    authorIds.add(value);
                }
            }
        }

        String error = bindAndValidate(req, book);
        if (error == null) {
            try {
                String cover = saveCover(req);
                if (cover != null) {
                    book.setCoverImage(cover);
                }
            } catch (IOException | IllegalArgumentException e) {
                error = "Upload ảnh thất bại: " + e.getMessage();
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("selectedAuthorIds", authorIds);
            showForm(req, resp, book);
            return;
        }

        if (isEdit) {
            bookService.update(book, authorIds);
            req.getSession().setAttribute("flash", "Đã cập nhật sách");
        } else {
            bookService.create(book, authorIds);
            req.getSession().setAttribute("flash", "Đã thêm sách mới");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/books");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Book_24162046 book)
            throws ServletException, IOException {
        if (req.getAttribute("selectedAuthorIds") == null) {
            req.setAttribute("selectedAuthorIds",
                    book.getAuthors().stream().map(a -> a.getAuthorId()).toList());
        }
        req.setAttribute("book", book);
        req.setAttribute("authors", authorService.findAll());
        req.getRequestDispatcher("/views/admin/book-form.jsp").forward(req, resp);
    }

    /** Gan du lieu form vao book, tra ve thong bao loi (null neu hop le). */
    private String bindAndValidate(HttpServletRequest req, Book_24162046 book) {
        String title = ParamUtil_24162046.trim(req.getParameter("title"));
        book.setTitle(title);
        book.setPublisher(ParamUtil_24162046.trim(req.getParameter("publisher")));
        book.setDescription(ParamUtil_24162046.trim(req.getParameter("description")));

        String coverUrl = ParamUtil_24162046.trim(req.getParameter("coverUrl"));
        if (!ParamUtil_24162046.isBlank(coverUrl)) {
            book.setCoverImage(coverUrl);
        }

        if (ParamUtil_24162046.isBlank(title)) {
            return "Tiêu đề không được để trống";
        }
        if (title.length() > 200) {
            return "Tiêu đề tối đa 200 ký tự";
        }
        if (book.getPublisher() != null && book.getPublisher().length() > 100) {
            return "Publisher tối đa 100 ký tự";
        }
        if (book.getCoverImage() != null && book.getCoverImage().length() > 100) {
            return "Đường dẫn ảnh bìa tối đa 100 ký tự";
        }
        try {
            String isbn = req.getParameter("isbn");
            book.setIsbn(ParamUtil_24162046.isBlank(isbn) ? null : Integer.valueOf(isbn.trim()));

            String quantity = req.getParameter("quantity");
            book.setQuantity(ParamUtil_24162046.isBlank(quantity) ? null : Integer.valueOf(quantity.trim()));
            if (book.getQuantity() != null && book.getQuantity() < 0) {
                return "Số lượng không được âm";
            }
        } catch (NumberFormatException e) {
            return "ISBN và số lượng phải là số nguyên";
        }
        try {
            String price = req.getParameter("price");
            book.setPrice(ParamUtil_24162046.isBlank(price) ? null : new BigDecimal(price.trim()));
            if (book.getPrice() != null
                    && (book.getPrice().signum() < 0 || book.getPrice().compareTo(new BigDecimal("9999.99")) > 0)) {
                return "Giá phải từ 0 đến 9999.99 (decimal(6,2))";
            }
        } catch (NumberFormatException e) {
            return "Giá không hợp lệ";
        }
        try {
            String date = req.getParameter("publishDate");
            book.setPublishDate(ParamUtil_24162046.isBlank(date) ? null : LocalDate.parse(date.trim()));
        } catch (RuntimeException e) {
            return "Ngày xuất bản không hợp lệ";
        }
        return null;
    }

    /** Luu file anh bia neu co upload, tra ve ten file (null neu khong upload). */
    private String saveCover(HttpServletRequest req) throws IOException, ServletException {
        Part part = req.getPart("coverFile");
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null) {
            return null;
        }
        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("chỉ chấp nhận file ảnh");
        }
        String original = Paths.get(part.getSubmittedFileName()).getFileName().toString()
                .replaceAll("[^A-Za-z0-9._-]", "_");
        String fileName = System.currentTimeMillis() + "_" + original;
        if (fileName.length() > 100) {
            fileName = fileName.substring(fileName.length() - 100);
        }
        Path dir = Paths.get(Constant_24162046.UPLOAD_DIR);
        Files.createDirectories(dir);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, dir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    private String action(HttpServletRequest req) {
        String path = req.getPathInfo();
        return path == null ? "/" : path;
    }
}
