package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /** Tim kiem theo ten, khong phan biet hoa thuong. */
    List<Category> findByCategorynameContainingIgnoreCaseOrderByCategoryidAsc(String keyword);

    /** Tim kiem co phan trang. */
    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable pageable);

    Optional<Category> findByCategorynameIgnoreCase(String categoryname);

    boolean existsByCategorynameIgnoreCase(String categoryname);

    List<Category> findByStatus(int status);
}
