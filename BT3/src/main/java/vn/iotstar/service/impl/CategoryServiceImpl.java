package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.impl.CategoryDao;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;

public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryDao categoryDao = new CategoryDao();

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void update(Category category) {
        // Giu lai anh cu khi nguoi dung khong upload anh moi
        if (category.getImages() == null) {
            Category old = categoryDao.findById(category.getCategoryid());
            if (old != null) {
                category.setImages(old.getImages());
            }
        }
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
        return categoryDao.findByCategoryname(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> searchByName(String catname) {
        return categoryDao.searchByName(catname);
    }

    @Override
    public List<Category> findAll(int page, int pagesize) {
        return categoryDao.findAll(page, pagesize);
    }

    @Override
    public int count() {
        return categoryDao.count();
    }
}
