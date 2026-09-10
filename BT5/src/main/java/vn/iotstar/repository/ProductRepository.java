package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByStatus(int status, Pageable pageable);

    List<Product> findByStatusOrderByCreatedDateDesc(int status, Pageable pageable);

    long countByStatus(int status);

    List<Product> findByCategory_Categoryid(int categoryId);

    List<Product> findByProductNameContainingIgnoreCaseOrderByProductIdAsc(String keyword);
}
