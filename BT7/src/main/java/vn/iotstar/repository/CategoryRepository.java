package vn.iotstar.repository;

import vn.iotstar.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByCategoryNameContainingIgnoreCase(String search, Pageable pageable);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
