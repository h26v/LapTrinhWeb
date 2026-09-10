package vn.iotstar.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.ProductService;
import vn.iotstar.util.ValidationUtil;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product insert(Product product) {
        validate(product);
        product.setProductId(0);
        product.setProductName(ValidationUtil.trim(product.getProductName()));
        product.setDescription(ValidationUtil.trim(product.getDescription()));
        if (product.getCreatedDate() == null) {
            product.setCreatedDate(new Date());
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        validate(product);

        Product existing = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay san pham."));

        existing.setProductName(ValidationUtil.trim(product.getProductName()));
        existing.setDescription(ValidationUtil.trim(product.getDescription()));
        existing.setPrice(product.getPrice());
        existing.setStatus(product.getStatus());
        existing.setCategory(product.getCategory());
        if (product.getImages() != null) {
            existing.setImages(product.getImages());
        }
        return productRepository.save(existing);
    }

    @Override
    public void delete(int id) throws Exception {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay san pham."));
        productRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findActive(Pageable pageable) {
        return productRepository.findByStatus(1, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return productRepository.countByStatus(1);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findNewestActive(int limit) {
        return productRepository.findByStatusOrderByCreatedDateDesc(1, PageRequest.of(0, Math.max(1, limit)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> search(String keyword) {
        if (ValidationUtil.isBlank(keyword)) {
            return findAll();
        }
        ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
        return productRepository.findByProductNameContainingIgnoreCaseOrderByProductIdAsc(keyword.trim());
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
