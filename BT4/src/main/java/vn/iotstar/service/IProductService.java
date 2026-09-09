package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Product;

public interface IProductService {
    void insert(Product product);

    void update(Product product);

    void delete(int id) throws Exception;

    Product findById(int id);

    List<Product> findAll();

    List<Product> findAll(int page, int pagesize);

    int count();

    List<Product> findActive(int page, int pagesize);

    int countActive();

    List<Product> findNewestActive(int limit);
}
