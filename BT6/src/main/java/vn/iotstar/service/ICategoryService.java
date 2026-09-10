package vn.iotstar.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface ICategoryService {

    Page<Category> findAll(Pageable pageable);

    /** Tim theo ten co phan trang; keyword rong thi tra ve tat ca. */
    Page<Category> search(String keyword, Pageable pageable);

    Optional<Category> findById(Long id);

    boolean isNameTaken(String name, Long excludeId);

    Category save(Category category);

    void deleteById(Long id);

    long count();
}
