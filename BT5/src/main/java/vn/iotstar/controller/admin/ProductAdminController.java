package vn.iotstar.controller.admin;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;
import vn.iotstar.util.ValidationUtil;

/**
 * CRUD Product cho admin, kem tim kiem theo ten san pham.
 */
@Controller
@RequestMapping("/admin")
public class ProductAdminController {

    private static final String SUB_DIR = "product";

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageUploadUtil imageUploadUtil;

    public ProductAdminController(ProductService productService, CategoryService categoryService,
            ImageUploadUtil imageUploadUtil) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageUploadUtil = imageUploadUtil;
    }

    @GetMapping("/products")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("products", productService.search(keyword));
        model.addAttribute("total", productService.count());
        model.addAttribute("keyword", ValidationUtil.trim(keyword));
        return "admin/product-list";
    }

    @GetMapping("/product/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-add";
    }

    @PostMapping("/product/insert")
    public String insert(@RequestParam(value = "productname", required = false) String productname,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "categoryid", required = false) String categoryid,
            @RequestParam(value = "images", required = false) MultipartFile images,
            Model model, RedirectAttributes redirect) {

        Product product = draftProduct(productname, description, price, status, categoryid);

        SavedFile upload = null;
        try {
            product = readProduct(product, price, categoryid);
            upload = imageUploadUtil.saveImage(images, SUB_DIR);
            if (upload != null) {
                product.setImages(upload.relativePath());
            }
            productService.insert(product);
            redirect.addFlashAttribute("success", "Da them san pham \"" + product.getProductName() + "\".");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            return failForm(model, product, price, e.getMessage(), "admin/product-add");
        } catch (IOException e) {
            deleteQuietly(upload);
            return failForm(model, product, price, "Khong the luu anh upload.", "admin/product-add");
        }
    }

    @GetMapping("/product/edit")
    public String editForm(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {

        int productId = ValidationUtil.parseId(id);
        Product product = productId > 0 ? productService.findById(productId) : null;
        if (product == null) {
            redirect.addFlashAttribute("error", "Khong tim thay san pham can sua.");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-edit";
    }

    @PostMapping("/product/update")
    public String update(@RequestParam(value = "productid", required = false) String productid,
            @RequestParam(value = "productname", required = false) String productname,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "categoryid", required = false) String categoryid,
            @RequestParam(value = "images", required = false) MultipartFile images,
            Model model, RedirectAttributes redirect) {

        Product product = draftProduct(productname, description, price, status, categoryid);
        product.setProductId(ValidationUtil.parseId(productid));

        SavedFile upload = null;
        String oldImage = null;
        try {
            Product old = productService.findById(product.getProductId());
            if (old == null) {
                redirect.addFlashAttribute("error", "Khong tim thay san pham can cap nhat.");
                return "redirect:/admin/products";
            }
            oldImage = old.getImages();

            product = readProduct(product, price, categoryid);
            product.setProductId(old.getProductId());
            product.setImages(oldImage);

            upload = imageUploadUtil.saveImage(images, SUB_DIR);
            if (upload != null) {
                product.setImages(upload.relativePath());
            }
            productService.update(product);
            if (upload != null) {
                imageUploadUtil.deleteLocalFile(oldImage);
            }
            redirect.addFlashAttribute("success", "Da cap nhat san pham \"" + product.getProductName() + "\".");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            product.setImages(oldImage);
            return failForm(model, product, price, e.getMessage(), "admin/product-edit");
        } catch (IOException e) {
            deleteQuietly(upload);
            product.setImages(oldImage);
            return failForm(model, product, price, "Khong the luu anh upload.", "admin/product-edit");
        }
    }

    @GetMapping("/product/delete")
    public String delete(@RequestParam(value = "id", required = false) String id, RedirectAttributes redirect) {
        int productId = ValidationUtil.parseId(id);
        if (productId <= 0) {
            redirect.addFlashAttribute("error", "Ma san pham khong hop le.");
            return "redirect:/admin/products";
        }
        try {
            Product product = productService.findById(productId);
            productService.delete(productId);
            if (product != null) {
                imageUploadUtil.deleteLocalFile(product.getImages());
            }
            redirect.addFlashAttribute("success", "Da xoa san pham.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Xoa san pham that bai.");
        }
        return "redirect:/admin/products";
    }

    /** Tao doi tuong nhap de hien lai form khi co loi. */
    private Product draftProduct(String productname, String description, String price, String status,
            String categoryid) {
        Product product = new Product();
        product.setProductName(ValidationUtil.trim(productname));
        product.setDescription(ValidationUtil.trim(description));
        try {
            if (!ValidationUtil.isBlank(price)) {
                product.setPrice(ValidationUtil.parsePrice(price));
            }
        } catch (IllegalArgumentException ignored) {
            // Gia sai dinh dang se bao loi khi validate chinh thuc.
        }
        try {
            product.setStatus(ValidationUtil.parseStatus(status));
        } catch (IllegalArgumentException ignored) {
            product.setStatus(1);
        }
        int categoryId = ValidationUtil.parseId(categoryid);
        if (categoryId > 0) {
            Category category = new Category();
            category.setCategoryid(categoryId);
            product.setCategory(category);
        }
        return product;
    }

    /** Doc va kiem tra du lieu tu form. */
    private Product readProduct(Product product, String price, String categoryid) {
        ValidationUtil.requireLength(product.getProductName(), "Ten san pham", 1, 255);
        ValidationUtil.requireMaxLength(product.getDescription(), "Mo ta", 1000);
        product.setPrice(ValidationUtil.parsePrice(price));

        int categoryId = ValidationUtil.requireId(categoryid, "Danh muc");
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Danh muc khong ton tai.");
        }
        product.setCategory(category);
        return product;
    }

    private String failForm(Model model, Product product, String price, String alert, String view) {
        model.addAttribute("alert", alert);
        model.addAttribute("product", product);
        // Giu nguyen chuoi gia nguoi dung da nhap de hien lai chinh xac.
        model.addAttribute("priceInput", ValidationUtil.trim(price));
        model.addAttribute("categories", categoryService.findAll());
        return view;
    }

    private void deleteQuietly(SavedFile upload) {
        if (upload == null) {
            return;
        }
        try {
            Path path = upload.file();
            imageUploadUtil.deleteIfExists(path);
        } catch (IOException ignored) {
            // Bo qua loi don file tam.
        }
    }
}
