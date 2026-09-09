package vn.iotstar.controller.product;

import java.io.IOException;
import java.math.BigDecimal;
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
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;

@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert",
        "/admin/product/edit", "/admin/product/update", "/admin/product/delete" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class ProductAdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PRODUCT_DIR = "product";
    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;

    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("add")) {
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
        } else if (url.contains("edit")) {
            int id = parseId(req.getParameter("id"));
            Product product = productService.findById(id);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
        } else if (url.contains("delete")) {
            delete(req, resp);
        } else {
            listProducts(req, resp);
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
            listProducts(req, resp);
        }
    }

    private void listProducts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> products = productService.findAll();
        req.setAttribute("products", products);
        req.setAttribute("total", productService.count());
        req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SavedFile upload = null;
        try {
            upload = saveUpload(req);
            Product product = readProduct(req);
            if (upload != null) {
                product.setImages(upload.relativePath());
            }
            productService.insert(product);
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            getServletContext().log("Them san pham that bai", e);
            forwardProductForm(req, resp, null, "Them san pham that bai: " + e.getMessage(),
                    "/views/admin/product-add.jsp");
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SavedFile upload = null;
        String oldImage = null;
        try {
            Product product = readProduct(req);
            product.setProductId(parseId(req.getParameter("productid")));
            Product old = productService.findById(product.getProductId());
            if (old == null) {
                throw new IllegalArgumentException("Khong tim thay san pham");
            }
            oldImage = old.getImages();
            upload = saveUpload(req);
            if (upload != null) {
                product.setImages(upload.relativePath());
            }
            productService.update(product);
            if (upload != null) {
                deleteLocalFile(oldImage);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            getServletContext().log("Cap nhat san pham that bai", e);
            Product product = productService.findById(parseId(req.getParameter("productid")));
            forwardProductForm(req, resp, product, "Cap nhat san pham that bai: " + e.getMessage(),
                    "/views/admin/product-edit.jsp");
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseId(req.getParameter("id"));
        try {
            Product product = productService.findById(id);
            productService.delete(id);
            deleteLocalFile(product == null ? null : product.getImages());
        } catch (Exception e) {
            req.getSession().setAttribute("alert", "Xoa san pham that bai: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private Product readProduct(HttpServletRequest req) {
        Product product = new Product();
        product.setProductName(trim(req.getParameter("productname")));
        product.setDescription(trim(req.getParameter("description")));
        product.setPrice(parsePrice(req.getParameter("price")));
        product.setStatus(parseStatus(req.getParameter("status")));

        Category category = new Category();
        category.setCategoryid(parseId(req.getParameter("categoryid")));
        product.setCategory(category);
        validate(product);
        return product;
    }

    private void validate(Product product) {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Ten san pham khong duoc de trong.");
        }
        if (product.getPrice() == null || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Gia san pham khong hop le.");
        }
        if (product.getCategory() == null || product.getCategory().getCategoryid() <= 0) {
            throw new IllegalArgumentException("Vui long chon danh muc.");
        }
    }

    private SavedFile saveUpload(HttpServletRequest req)
            throws ServletException, IOException, UploadValidationException {
        Part part = req.getPart("images");
        return ImageUploadUtil.saveImage(part, PRODUCT_DIR, MAX_IMAGE_BYTES);
    }

    private void forwardProductForm(HttpServletRequest req, HttpServletResponse resp, Product product,
            String alert, String view) throws ServletException, IOException {
        req.setAttribute("alert", alert);
        req.setAttribute("product", product);
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher(view).forward(req, resp);
    }

    private BigDecimal parsePrice(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }

    private int parseStatus(String status) {
        return "1".equals(status) ? 1 : 0;
    }

    private int parseId(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return 0;
            }
            return Integer.parseInt(id.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private void deleteLocalFile(String relativePath) {
        try {
            ImageUploadUtil.deleteLocalFile(relativePath);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file san pham", e);
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
