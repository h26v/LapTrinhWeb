package vn.iotstar.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import vn.iotstar.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            select u from User u
            where lower(u.username) like lower(concat('%', :keyword, '%'))
               or lower(u.email) like lower(concat('%', :keyword, '%'))
               or lower(u.fullName) like lower(concat('%', :keyword, '%'))
            """)
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("select count(p) from Product p where p.user.id = :userId")
    long countProductsByUserId(@Param("userId") Long userId);
}
