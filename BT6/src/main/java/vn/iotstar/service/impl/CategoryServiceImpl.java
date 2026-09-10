package vn.iotstar.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.ICategoryService;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public boolean isNameTaken(String name, Long excludeId) {
        String trimmed = name == null ? "" : name.trim();
        return excludeId == null
                ? categoryRepository.existsByCategoryNameIgnoreCase(trimmed)
                : categoryRepository.existsByCategoryNameIgnoreCaseAndCategoryIdNot(trimmed, excludeId);
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }
}
