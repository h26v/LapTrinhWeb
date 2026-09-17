package vn.iotstar.repository;

import vn.iotstar.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductNameIgnoreCase(String productName);

    @Query("""
            select p from Product p
            where (:search = '' or lower(p.productName) like lower(concat('%', :search, '%')))
              and (:categoryId is null or p.category.categoryId = :categoryId)
            """)
    Page<Product> search(@Param("search") String search,
                         @Param("categoryId") Long categoryId,
                         Pageable pageable);

    long countByCategory_CategoryId(Long categoryId);
}
