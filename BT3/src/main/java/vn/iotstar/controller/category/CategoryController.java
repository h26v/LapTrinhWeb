package vn.iotstar.controller.category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
@MultipartConfig(maxFileSize = 16 * 1024 * 1024, maxRequestSize = 17 * 1024 * 1024)
public class CategoryController extends HttpServlet {
    private static final String SUB_DIR = "category";

    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("add")) {
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
        } else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = categoryService.findById(id);
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
        } else if (url.contains("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                Category category = categoryService.findById(id);
                categoryService.delete(id);
                deleteFile(category == null ? null : category.getImages());
            } catch (Exception e) {
                req.getSession().setAttribute("alert", "Xoa danh muc that bai: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } else {
            listCategories(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("insert")) {
            insert(req, resp);
        } else if (url.contains("update")) {
            update(req, resp);
        } else {
            listCategories(req, resp);
        }
    }

    private void listCategories(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Category> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = categoryService.searchByName(keyword.trim());
            req.setAttribute("keyword", keyword.trim());
        } else {
            list = categoryService.findAll();
        }
        req.setAttribute("categories", list);
        req.setAttribute("total", categoryService.count());
        req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category category = new Category();
        category.setCategoryname(req.getParameter("categoryname"));
        category.setStatus(parseStatus(req.getParameter("status")));
        category.setImages(saveUpload(req));
        categoryService.insert(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category category = new Category();
        category.setCategoryid(Integer.parseInt(req.getParameter("categoryid")));
        category.setCategoryname(req.getParameter("categoryname"));
        category.setStatus(parseStatus(req.getParameter("status")));

        String newImage = saveUpload(req);
        if (newImage != null) {
            // Anh moi duoc tai len -> xoa anh cu
            Category old = categoryService.findById(category.getCategoryid());
            if (old != null) {
                deleteFile(old.getImages());
            }
            category.setImages(newImage);
        }
        // newImage == null -> service giu lai anh cu
        categoryService.update(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private int parseStatus(String status) {
        return "1".equals(status) ? 1 : 0;
    }

    /**
     * Luu file upload vao Constant.DIR/category va tra ve duong dan tuong doi.
     * Tra ve null neu nguoi dung khong chon file.
     */
    private String saveUpload(HttpServletRequest req) throws ServletException, IOException {
        Part part = req.getPart("images1");
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String submitted = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        if (submitted.isEmpty()) {
            return null;
        }
        String fileName = System.currentTimeMillis() + "_" + submitted;

        File dir = new File(Constant.DIR, SUB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        part.write(new File(dir, fileName).getAbsolutePath());
        return SUB_DIR + "/" + fileName;
    }

    private void deleteFile(String relativePath) {
        if (relativePath == null || relativePath.startsWith("https")) {
            return;
        }
        try {
            Files.deleteIfExists(Paths.get(Constant.DIR, relativePath));
        } catch (IOException ignored) {
            // Khong the xoa file cu -> bo qua, khong lam gian doan luong CRUD
        }
    }
}
