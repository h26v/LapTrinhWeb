package vn.iotstar.repository;

import vn.iotstar.entity.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Page<User> findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String email, String fullName, Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            JOIN FETCH u.role
            WHERE u.email = :email
            """)
    Optional<User> findByEmailWithRole(@Param("email") String email);
}
