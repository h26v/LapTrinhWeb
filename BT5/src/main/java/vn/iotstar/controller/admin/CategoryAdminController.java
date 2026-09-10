package vn.iotstar.controller.admin;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;
import vn.iotstar.util.ValidationUtil;

/**
 * CRUD Category cho admin.
 *
 * <p>Yeu cau quyen admin duoc dam bao boi AdminInterceptor tren /admin/**.
 */
@Controller
@RequestMapping("/admin")
public class CategoryAdminController {

    private static final String SUB_DIR = "category";
    private static final int PAGE_SIZE = 5;

    private final CategoryService categoryService;
    private final ImageUploadUtil imageUploadUtil;

    public CategoryAdminController(CategoryService categoryService, ImageUploadUtil imageUploadUtil) {
        this.categoryService = categoryService;
        this.imageUploadUtil = imageUploadUtil;
    }

    // ===== Danh sach + tim kiem =====

    @GetMapping("/categories")
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page, Model model) {

        int currentPage = Math.max(1, page);
        Pageable pageable = PageRequest.of(currentPage - 1, PAGE_SIZE);

        Page<Category> result = categoryService.search(keyword, pageable);
        model.addAttribute("categories", result.getContent());
        model.addAttribute("total", categoryService.count());
        model.addAttribute("resultSize", result.getTotalElements());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("keyword", ValidationUtil.trim(keyword));
        return "admin/category-list";
    }

    // ===== Them =====

    @GetMapping("/category/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @PostMapping("/category/insert")
    public String insert(@RequestParam(value = "categoryname", required = false) String categoryname,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "images1", required = false) MultipartFile images1,
            Model model, RedirectAttributes redirect) {

        Category category = new Category();
        category.setCategoryname(ValidationUtil.trim(categoryname));
        category.setStatus(parseStatusOrDefault(status));

        SavedFile upload = null;
        try {
            upload = imageUploadUtil.saveImage(images1, SUB_DIR);
            if (upload != null) {
                category.setImages(upload.relativePath());
            }
            categoryService.insert(category);
            redirect.addFlashAttribute("success", "Da them danh muc \"" + category.getCategoryname() + "\".");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            model.addAttribute("alert", e.getMessage());
            model.addAttribute("category", category);
            return "admin/category-add";
        } catch (IOException e) {
            deleteQuietly(upload);
            model.addAttribute("alert", "Khong the luu anh upload.");
            model.addAttribute("category", category);
            return "admin/category-add";
        }
    }

    // ===== Sua =====

    @GetMapping("/category/edit")
    public String editForm(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {

        int categoryId = ValidationUtil.parseId(id);
        Category category = categoryId > 0 ? categoryService.findById(categoryId) : null;
        if (category == null) {
            redirect.addFlashAttribute("error", "Khong tim thay danh muc can sua.");
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        return "admin/category-edit";
    }

    @PostMapping("/category/update")
    public String update(@RequestParam(value = "categoryid", required = false) String categoryid,
            @RequestParam(value = "categoryname", required = false) String categoryname,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "images", required = false) String images,
            @RequestParam(value = "images1", required = false) MultipartFile images1,
            Model model, RedirectAttributes redirect) {

        Category category = new Category();
        category.setCategoryid(ValidationUtil.parseId(categoryid));
        category.setCategoryname(ValidationUtil.trim(categoryname));
        category.setStatus(parseStatusOrDefault(status));
        category.setImages(ValidationUtil.trim(images));

        SavedFile upload = null;
        String oldImage = null;
        try {
            Category old = categoryService.findById(category.getCategoryid());
            if (old == null) {
                redirect.addFlashAttribute("error", "Khong tim thay danh muc can cap nhat.");
                return "redirect:/admin/categories";
            }
            oldImage = old.getImages();

            upload = imageUploadUtil.saveImage(images1, SUB_DIR);
            if (upload != null) {
                category.setImages(upload.relativePath());
            }

            categoryService.update(category);
            // Chi xoa anh cu sau khi database da cap nhat thanh cong.
            if (upload != null) {
                imageUploadUtil.deleteLocalFile(oldImage);
            }
            redirect.addFlashAttribute("success", "Da cap nhat danh muc \"" + category.getCategoryname() + "\".");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            // Hien lai anh cu neu vua upload anh moi that bai.
            category.setImages(oldImage);
            model.addAttribute("alert", e.getMessage());
            model.addAttribute("category", category);
            return "admin/category-edit";
        } catch (IOException e) {
            deleteQuietly(upload);
            category.setImages(oldImage);
            model.addAttribute("alert", "Khong the luu anh upload.");
            model.addAttribute("category", category);
            return "admin/category-edit";
        }
    }

    // ===== Xoa =====

    @GetMapping("/category/delete")
    public String delete(@RequestParam(value = "id", required = false) String id, RedirectAttributes redirect) {
        int categoryId = ValidationUtil.parseId(id);
        if (categoryId <= 0) {
            redirect.addFlashAttribute("error", "Ma danh muc khong hop le.");
            return "redirect:/admin/categories";
        }
        try {
            Category category = categoryService.findById(categoryId);
            categoryService.delete(categoryId);
            if (category != null) {
                imageUploadUtil.deleteLocalFile(category.getImages());
            }
            redirect.addFlashAttribute("success", "Da xoa danh muc.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Xoa danh muc that bai.");
        }
        return "redirect:/admin/categories";
    }

    private int parseStatusOrDefault(String status) {
        try {
            return ValidationUtil.parseStatus(status);
        } catch (IllegalArgumentException e) {
            return 1;
        }
    }

    private void deleteQuietly(SavedFile upload) {
        if (upload == null) {
            return;
        }
        try {
            Path path = upload.file();
            imageUploadUtil.deleteIfExists(path);
        } catch (IOException ignored) {
            // Khong the don file tam, bo qua de khong che mat loi chinh.
        }
    }
}
