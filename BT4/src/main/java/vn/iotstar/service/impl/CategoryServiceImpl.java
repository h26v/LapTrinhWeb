package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.impl.CategoryDao;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.ValidationUtil;

public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryDao categoryDao = new CategoryDao();

    @Override
    public void insert(Category category) {
        validate(category);
        if (findByCategoryname(category.getCategoryname()) != null) {
            throw new IllegalArgumentException("Ten danh muc da ton tai.");
        }
        category.setCategoryname(ValidationUtil.trim(category.getCategoryname()));
        categoryDao.insert(category);
    }

    @Override
    public void update(Category category) {
        validate(category);
        Category existed = findByCategoryname(category.getCategoryname());
        if (existed != null && existed.getCategoryid() != category.getCategoryid()) {
            throw new IllegalArgumentException("Ten danh muc da ton tai.");
        }

        // Giu lai anh cu khi nguoi dung khong upload anh moi
        if (category.getImages() == null) {
            Category old = categoryDao.findById(category.getCategoryid());
            if (old != null) {
                category.setImages(old.getImages());
            }
        }
        category.setCategoryname(ValidationUtil.trim(category.getCategoryname()));
        categoryDao.update(category);
    }

    @Override
    public void delete(int cateid) throws Exception {
        categoryDao.delete(cateid);
    }

    @Override
    public Category findById(int cateid) {
        return categoryDao.findById(cateid);
    }

    @Override
    public Category findByCategoryname(String name) {
        if (ValidationUtil.isBlank(name)) {
            return null;
        }
        return categoryDao.findByCategoryname(name.trim());
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> searchByName(String catname) {
        if (ValidationUtil.isBlank(catname)) {
            return findAll();
        }
        ValidationUtil.requireMaxLength(catname, "Tu khoa tim kiem", 100);
        return categoryDao.searchByName(catname.trim());
    }

    @Override
    public List<Category> findAll(int page, int pagesize) {
        return categoryDao.findAll(page, pagesize);
    }

    @Override
    public int count() {
        return categoryDao.count();
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
