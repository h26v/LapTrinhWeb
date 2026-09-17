package vn.iotstar.service;

import vn.iotstar.dto.PageResponse;
import vn.iotstar.dto.ProductRequest;
import vn.iotstar.dto.ProductResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.exception.ConflictException;
import vn.iotstar.exception.ResourceNotFoundException;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> search(String search, Long categoryId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createDate").and(Sort.by(Sort.Direction.ASC, "productName")));
        Page<Product> products = productRepository.search(search == null ? "" : search.trim(), categoryId, pageable);
        return PageResponse.from(products, this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public ProductResponse create(ProductRequest request) {
        ensureNameIsAvailable(request.productName(), null);
        Product product = new Product();
        apply(product, request);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findEntity(id);
        ensureNameIsAvailable(request.productName(), id);
        apply(product, request);
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(findEntity(id));
    }

    private void apply(Product product, ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy category với id " + request.categoryId()));
        product.setProductName(request.productName().trim());
        product.setImages(normalize(request.images()));
        product.setUnitPrice(request.unitPrice().setScale(2));
        product.setDiscount(request.discount() == null ? BigDecimal.ZERO : request.discount().setScale(2));
        product.setDescription(normalize(request.description()));
        product.setCategory(category);
        product.setQuantity(request.quantity());
        product.setStatus(request.status());
    }

    private Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy product với id " + id));
    }

    private void ensureNameIsAvailable(String name, Long ignoredId) {
        productRepository.findByProductNameIgnoreCase(name.trim()).ifPresent(existing -> {
            if (ignoredId == null || !existing.getProductId().equals(ignoredId)) {
                throw new ConflictException("Product '" + name.trim() + "' đã tồn tại");
            }
        });
    }

    private ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        return new ProductResponse(product.getProductId(), product.getProductName(), product.getImages(),
                product.getUnitPrice(), product.getDiscount(), product.getDescription(), category.getCategoryId(),
                category.getCategoryName(), product.getQuantity(), product.getStatus(), product.getCreateDate(),
                product.getUpdateDate());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
