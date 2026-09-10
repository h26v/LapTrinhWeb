package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.User;

public interface UserService {

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User findById(int id);

    /**
     * Tim kiem theo username, ho ten, email hoac so dien thoai.
     * Tra tat ca neu tu khoa rong.
     */
    List<User> search(String keyword);

    Page<User> search(String keyword, Pageable pageable);

    User findByUsername(String username);

    User insert(User user);

    User update(User user);

    void delete(int id) throws Exception;

    long count();

    /** Doi mat khau cho mot tai khoan (admin reset). */
    void updatePassword(int id, String rawPassword);
}
