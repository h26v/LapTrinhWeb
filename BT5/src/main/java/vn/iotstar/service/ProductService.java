package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Product;

public interface ProductService {

    List<Product> findAll();

    Product findById(int id);

    Product insert(Product product);

    Product update(Product product);

    void delete(int id) throws Exception;

    long count();

    Page<Product> findActive(Pageable pageable);

    long countActive();

    List<Product> findNewestActive(int limit);

    /** Tim kiem theo ten san pham. */
    List<Product> search(String keyword);
}
