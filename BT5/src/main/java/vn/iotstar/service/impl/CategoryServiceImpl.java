package vn.iotstar.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.ValidationUtil;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> search(String keyword) {
        if (ValidationUtil.isBlank(keyword)) {
            return findAll();
        }
        ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
        return categoryRepository.findByCategorynameContainingIgnoreCaseOrderByCategoryidAsc(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> search(String keyword, Pageable pageable) {
        if (ValidationUtil.isBlank(keyword)) {
            return findAll(pageable);
        }
        ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
        return categoryRepository.findByCategorynameContainingIgnoreCase(keyword.trim(), pageable);
    }

    @Override
    public Category insert(Category category) {
        validate(category);
        String name = ValidationUtil.trim(category.getCategoryname());
        if (categoryRepository.existsByCategorynameIgnoreCase(name)) {
            throw new IllegalArgumentException("Ten danh muc da ton tai.");
        }
        category.setCategoryid(0);
        category.setCategoryname(name);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        validate(category);

        // Nap entity dang duoc quan ly de tranh merge mot doi tuong detached thieu truong.
        Category existing = categoryRepository.findById(category.getCategoryid())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay danh muc."));

        String name = ValidationUtil.trim(category.getCategoryname());
        categoryRepository.findByCategorynameIgnoreCase(name)
                .filter(found -> found.getCategoryid() != category.getCategoryid())
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Ten danh muc da ton tai.");
                });

        existing.setCategoryname(name);
        existing.setStatus(category.getStatus());
        // Chi thay anh khi nguoi dung upload anh moi; neu khong thi giu anh cu.
        if (category.getImages() != null) {
            existing.setImages(category.getImages());
        }
        return categoryRepository.save(existing);
    }

    @Override
    public void delete(int id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay danh muc."));

        // Chan xoa danh muc con san pham de tranh loi khoa ngoai.
        int productCount = category.getProducts().size();
        if (productCount > 0) {
            throw new IllegalArgumentException(
                    "Khong the xoa: danh muc dang co " + productCount + " san pham.");
        }
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }

    private void validate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Danh muc khong hop le.");
        }
        ValidationUtil.requireLength(category.getCategoryname(), "Ten danh muc", 1, 255);
        if (category.getStatus() != 0 && category.getStatus() != 1) {
            throw new IllegalArgumentException("Trang thai khong hop le.");
        }
    }
}
