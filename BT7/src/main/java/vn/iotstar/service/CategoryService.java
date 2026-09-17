package vn.iotstar.service;

import vn.iotstar.dto.CategoryRequest;
import vn.iotstar.dto.CategoryResponse;
import vn.iotstar.dto.PageResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.exception.ConflictException;
import vn.iotstar.exception.ResourceNotFoundException;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> search(String search, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "categoryName"));
        Page<Category> categories = categoryRepository.findByCategoryNameContainingIgnoreCase(
                search == null ? "" : search.trim(), pageable);
        return PageResponse.from(categories, this::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        ensureNameIsAvailable(request.categoryName(), null);
        Category category = new Category(request.categoryName().trim(), normalize(request.icon()));
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findEntity(id);
        ensureNameIsAvailable(request.categoryName(), id);
        category.setCategoryName(request.categoryName().trim());
        category.setIcon(normalize(request.icon()));
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = findEntity(id);
        long productCount = productRepository.countByCategory_CategoryId(id);
        if (productCount > 0) {
            throw new ConflictException("Không thể xóa category đang có " + productCount + " product");
        }
        categoryRepository.delete(category);
    }

    private Category findEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy category với id " + id));
    }

    private void ensureNameIsAvailable(String name, Long ignoredId) {
        categoryRepository.findByCategoryNameIgnoreCase(name.trim()).ifPresent(existing -> {
            if (ignoredId == null || !existing.getCategoryId().equals(ignoredId)) {
                throw new ConflictException("Category '" + name.trim() + "' đã tồn tại");
            }
        });
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getCategoryId(), category.getCategoryName(), category.getIcon(),
                productRepository.countByCategory_CategoryId(category.getCategoryId()));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
