package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.impl.ProductDao;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.util.ValidationUtil;

public class ProductServiceImpl implements IProductService {
    private final IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        validate(product);
        product.setProductName(ValidationUtil.trim(product.getProductName()));
        product.setDescription(ValidationUtil.trim(product.getDescription()));
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        validate(product);
        if (product.getImages() == null) {
            Product old = productDao.findById(product.getProductId());
            if (old != null) {
                product.setImages(old.getImages());
            }
        }
        product.setProductName(ValidationUtil.trim(product.getProductName()));
        product.setDescription(ValidationUtil.trim(product.getDescription()));
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

    private void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("San pham khong hop le.");
        }
        ValidationUtil.requireLength(product.getProductName(), "Ten san pham", 1, 255);
        ValidationUtil.requireMaxLength(product.getDescription(), "Mo ta", 1000);
        if (product.getPrice() == null || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Gia san pham khong hop le.");
        }
        if (product.getStatus() != 0 && product.getStatus() != 1) {
            throw new IllegalArgumentException("Trang thai khong hop le.");
        }
        if (product.getCategory() == null || product.getCategory().getCategoryid() <= 0) {
            throw new IllegalArgumentException("Vui long chon danh muc.");
        }
    }
}
