package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /** Tim kiem theo ten (khong phan biet hoa thuong) co phan trang. */
    Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);

    /** Kiem tra trung ten khi them moi. */
    boolean existsByCategoryNameIgnoreCase(String name);

    /** Kiem tra trung ten khi sua (bo qua chinh ban ghi dang sua). */
    boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String name, Long categoryId);
}
