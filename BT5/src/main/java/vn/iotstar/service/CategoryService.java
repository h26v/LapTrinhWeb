package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface CategoryService {

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    Category findById(int id);

    /** Tim kiem theo ten; tra tat ca neu tu khoa rong. */
    List<Category> search(String keyword);

    Page<Category> search(String keyword, Pageable pageable);

    Category insert(Category category);

    Category update(Category category);

    void delete(int id) throws Exception;

    long count();
}
