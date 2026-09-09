package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.impl.ProductDao;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;

public class ProductServiceImpl implements IProductService {
    private final IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        if (product.getImages() == null) {
            Product old = productDao.findById(product.getProductId());
            if (old != null) {
                product.setImages(old.getImages());
            }
        }
        productDao.update(product);
    }

    @Override
    public void delete(int id) throws Exception {
        productDao.delete(id);
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findAll(int page, int pagesize) {
        return productDao.findAll(page, pagesize);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public List<Product> findActive(int page, int pagesize) {
        return productDao.findActive(page, pagesize);
    }

    @Override
    public int countActive() {
        return productDao.countActive();
    }

    @Override
    public List<Product> findNewestActive(int limit) {
        return productDao.findNewestActive(limit);
    }
}
