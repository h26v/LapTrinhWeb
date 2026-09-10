package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameIgnoreCase(String userName);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByEmailIgnoreCase(String email);

    /**
     * Tim kiem theo username, ho ten, email hoac so dien thoai.
     * Dung JPQL de gop nhieu truong trong mot dieu kien.
     */
    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.email)    LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR u.phone           LIKE CONCAT('%', :keyword, '%')
            ORDER BY u.id ASC
            """)
    List<User> search(@Param("keyword") String keyword);

    /** Nhu tren nhung co phan trang. */
    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.email)    LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR u.phone           LIKE CONCAT('%', :keyword, '%')
            """)
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);

    long countByRoleid(int roleid);
}
