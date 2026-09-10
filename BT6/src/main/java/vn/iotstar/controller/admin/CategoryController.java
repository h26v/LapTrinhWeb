package vn.iotstar.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;

/**
 * CRUD + tim kiem phan trang cho Category.
 *
 * <p>Sau moi thao tac ghi (them/sua/xoa) dung redirect + flash attribute (PRG)
 * de F5 khong gui lai form.
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    /** Cac lua chon so dong/trang hien tren giao dien. */
    static final List<Integer> PAGE_SIZES = List.of(3, 5, 10, 15, 20);
    private static final Set<Integer> ALLOWED_SIZES = Set.copyOf(PAGE_SIZES);
    private static final int DEFAULT_SIZE = 5;
    private static final String VIEW_FORM = "admin/categories/addOrEdit";
    private static final String REDIRECT_LIST = "redirect:/admin/categories/searchpaginated";

    private final ICategoryService categoryService;
    private final ImageUploadUtil imageUploadUtil;

    public CategoryController(ICategoryService categoryService, ImageUploadUtil imageUploadUtil) {
        this.categoryService = categoryService;
        this.imageUploadUtil = imageUploadUtil;
    }

    @GetMapping({ "", "/" })
    public String index() {
        return REDIRECT_LIST;
    }

    /** Danh sach + tim kiem theo ten + phan trang. Trang bat dau tu 1. */
    @GetMapping("/searchpaginated")
    public String search(Model model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size) {

        String keyword = StringUtils.hasText(name) ? name.trim() : "";
        int pageSize = size.filter(ALLOWED_SIZES::contains).orElse(DEFAULT_SIZE);
        int currentPage = Math.max(1, page.orElse(1));
        Sort sort = Sort.by("categoryName").ascending().and(Sort.by("categoryId"));

        Page<Category> resultPage = categoryService.search(keyword, PageRequest.of(currentPage - 1, pageSize, sort));
        // Trang vuot qua so trang (vi du vua xoa ban ghi cuoi cua trang cuoi) -> lui ve trang cuoi.
        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            resultPage = categoryService.search(keyword, PageRequest.of(currentPage - 1, pageSize, sort));
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            // Chi hien toi da 5 so trang quanh trang hien tai.
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(totalPages, currentPage + 2);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("name", keyword);
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "admin/categories/searchpaginated";
    }

    @GetMapping("/add")
    public String add(Model model) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setIsEdit(false);
        model.addAttribute("category", categoryModel);
        return VIEW_FORM;
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(@PathVariable("categoryId") Long categoryId, Model model, RedirectAttributes redirect) {
        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            redirect.addFlashAttribute("error", "Danh mục #" + categoryId + " không tồn tại.");
            return REDIRECT_LIST;
        }
        CategoryModel categoryModel = toModel(opt.get());
        categoryModel.setIsEdit(true);
        model.addAttribute("category", categoryModel);
        return VIEW_FORM;
    }

    @GetMapping("/view/{categoryId}")
    public String view(@PathVariable("categoryId") Long categoryId, Model model, RedirectAttributes redirect) {
        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            redirect.addFlashAttribute("error", "Danh mục #" + categoryId + " không tồn tại.");
            return REDIRECT_LIST;
        }
        model.addAttribute("category", opt.get());
        return "admin/categories/view";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(@Valid @ModelAttribute("category") CategoryModel categoryModel,
            BindingResult result,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            Model model, RedirectAttributes redirect) {

        boolean isEdit = categoryModel.getIsEdit();
        Category entity;
        if (isEdit) {
            Optional<Category> opt = categoryModel.getCategoryId() == null
                    ? Optional.empty()
                    : categoryService.findById(categoryModel.getCategoryId());
            if (opt.isEmpty()) {
                redirect.addFlashAttribute("error", "Danh mục cần sửa không tồn tại.");
                return REDIRECT_LIST;
            }
            entity = opt.get();
            // Anh hien tai lay tu database, khong tin gia tri an tu form.
            categoryModel.setImages(entity.getImages());
        } else {
            entity = new Category();
            categoryModel.setCategoryId(null);
            categoryModel.setImages(null);
        }

        if (!result.hasFieldErrors("categoryName")
                && categoryService.isNameTaken(categoryModel.getCategoryName(), entity.getCategoryId())) {
            result.rejectValue("categoryName", "duplicate", "Tên danh mục đã tồn tại");
        }
        if (result.hasErrors()) {
            return VIEW_FORM;
        }

        SavedFile saved;
        try {
            saved = imageUploadUtil.saveImage(imageFile, "category");
        } catch (UploadValidationException e) {
            result.rejectValue("images", "upload", e.getMessage());
            return VIEW_FORM;
        } catch (IOException e) {
            log.error("Khong luu duoc anh category", e);
            result.rejectValue("images", "upload", "Không lưu được ảnh, vui lòng thử lại.");
            return VIEW_FORM;
        }

        String oldImage = entity.getImages();
        entity.setCategoryName(categoryModel.getCategoryName().trim());
        entity.setStatus(categoryModel.getStatus());
        if (saved != null) {
            entity.setImages(saved.relativePath());
        }

        try {
            categoryService.save(entity);
        } catch (RuntimeException e) {
            // Luu DB that bai thi xoa anh vua upload de khong de lai file rac.
            deleteQuietly(saved == null ? null : saved.relativePath());
            throw e;
        }
        if (saved != null) {
            deleteQuietly(oldImage);
        }

        redirect.addFlashAttribute("message", isEdit
                ? "Đã cập nhật danh mục \"" + entity.getCategoryName() + "\"."
                : "Đã thêm danh mục \"" + entity.getCategoryName() + "\".");
        return REDIRECT_LIST;
    }

    /** Xoa bang POST (khong dung GET) de link/crawler khong vo tinh xoa du lieu. */
    @PostMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            RedirectAttributes redirect) {

        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            redirect.addFlashAttribute("error", "Danh mục #" + categoryId + " không tồn tại.");
        } else {
            Category category = opt.get();
            categoryService.deleteById(categoryId);
            deleteQuietly(category.getImages());
            redirect.addFlashAttribute("message", "Đã xóa danh mục \"" + category.getCategoryName() + "\".");
        }

        // Quay lai dung trang/tu khoa dang xem.
        if (StringUtils.hasText(name)) {
            redirect.addAttribute("name", name);
        }
        if (page != null) {
            redirect.addAttribute("page", page);
        }
        if (size != null) {
            redirect.addAttribute("size", size);
        }
        return REDIRECT_LIST;
    }

    private CategoryModel toModel(Category entity) {
        CategoryModel m = new CategoryModel();
        m.setCategoryId(entity.getCategoryId());
        m.setCategoryName(entity.getCategoryName());
        m.setImages(entity.getImages());
        m.setStatus(entity.getStatus());
        return m;
    }

    private void deleteQuietly(String relativePath) {
        try {
            imageUploadUtil.deleteLocalFile(relativePath);
        } catch (IOException e) {
            log.warn("Khong xoa duoc anh {}", relativePath, e);
        }
    }
}
