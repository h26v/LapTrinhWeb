package vn.iotstar.controller.category;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;
import vn.iotstar.util.ValidationUtil;

@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SUB_DIR = "category";
    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;

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
            edit(req, resp);
        } else if (url.contains("delete")) {
            delete(req, resp);
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
        String keyword = ValidationUtil.trim(req.getParameter("keyword"));
        List<Category> list;
        try {
            if (!ValidationUtil.isBlank(keyword)) {
                ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
                list = categoryService.searchByName(keyword);
                req.setAttribute("keyword", keyword);
            } else {
                list = categoryService.findAll();
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("alert", e.getMessage());
            list = categoryService.findAll();
        }
        req.setAttribute("categories", list);
        req.setAttribute("total", categoryService.count());
        req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = ValidationUtil.parseId(req.getParameter("id"));
        if (id <= 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Category category = categoryService.findById(id);
        if (category == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category category = new Category();
        SavedFile upload = null;
        try {
            readCategory(req, category, false);
            upload = saveUpload(req);
            if (upload != null) {
                category.setImages(upload.relativePath());
            }
            categoryService.insert(category);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            getServletContext().log("Them danh muc that bai", e);
            forwardCategoryForm(req, resp, category, "Them danh muc that bai: " + e.getMessage(),
                    "/views/admin/category-add.jsp");
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category category = new Category();
        Category old = null;
        SavedFile upload = null;
        try {
            readCategory(req, category, true);
            old = categoryService.findById(category.getCategoryid());
            if (old == null) {
                throw new IllegalArgumentException("Khong tim thay danh muc");
            }
            category.setImages(old.getImages());
            upload = saveUpload(req);
            if (upload != null) {
                category.setImages(upload.relativePath());
            }
            categoryService.update(category);
            if (upload != null) {
                deleteLocalFile(old.getImages());
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            if (old != null) {
                category.setImages(old.getImages());
            }
            getServletContext().log("Cap nhat danh muc that bai", e);
            forwardCategoryForm(req, resp, category, "Cap nhat danh muc that bai: " + e.getMessage(),
                    "/views/admin/category-edit.jsp");
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = ValidationUtil.parseId(req.getParameter("id"));
        if (id <= 0) {
            req.getSession().setAttribute("alert", "Ma danh muc khong hop le.");
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
            return;
        }
        try {
            Category category = categoryService.findById(id);
            categoryService.delete(id);
            deleteLocalFile(category == null ? null : category.getImages());
        } catch (Exception e) {
            req.getSession().setAttribute("alert", "Xoa danh muc that bai: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void readCategory(HttpServletRequest req, Category category, boolean requireId) {
        if (requireId) {
            category.setCategoryid(ValidationUtil.requireId(req.getParameter("categoryid"), "Ma danh muc"));
        }
        category.setCategoryname(ValidationUtil.trim(req.getParameter("categoryname")));
        category.setStatus(ValidationUtil.parseStatus(req.getParameter("status")));
        ValidationUtil.requireLength(category.getCategoryname(), "Ten danh muc", 1, 255);
    }

    private SavedFile saveUpload(HttpServletRequest req)
            throws ServletException, IOException, UploadValidationException {
        Part part = req.getPart("images1");
        if (part == null || part.getSize() == 0) {
            part = req.getPart("images");
        }
        return ImageUploadUtil.saveImage(part, SUB_DIR, MAX_IMAGE_BYTES);
    }

    private void forwardCategoryForm(HttpServletRequest req, HttpServletResponse resp, Category category,
            String alert, String view) throws ServletException, IOException {
        req.setAttribute("alert", alert);
        req.setAttribute("category", category);
        req.getRequestDispatcher(view).forward(req, resp);
    }

    private void deleteLocalFile(String relativePath) {
        try {
            ImageUploadUtil.deleteLocalFile(relativePath);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file danh muc", e);
        }
    }

    private void deleteIfUploaded(Path file) {
        try {
            ImageUploadUtil.deleteIfExists(file);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file upload moi", e);
        }
    }
}
