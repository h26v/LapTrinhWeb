package vn.iotstar.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PasswordUtil;
import vn.iotstar.util.ValidationUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> search(String keyword) {
        if (ValidationUtil.isBlank(keyword)) {
            return findAll();
        }
        ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
        return userRepository.search(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> search(String keyword, Pageable pageable) {
        if (ValidationUtil.isBlank(keyword)) {
            return findAll(pageable);
        }
        ValidationUtil.requireMaxLength(keyword, "Tu khoa tim kiem", 100);
        return userRepository.search(keyword.trim(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (ValidationUtil.isBlank(username)) {
            return null;
        }
        return userRepository.findByUserNameIgnoreCase(username.trim()).orElse(null);
    }

    @Override
    public User insert(User user) {
        validateForInsert(user);

        String username = ValidationUtil.trim(user.getUserName());
        String email = ValidationUtil.trim(user.getEmail());
        if (userRepository.existsByUserNameIgnoreCase(username)) {
            throw new IllegalArgumentException("Ten dang nhap da ton tai.");
        }
        if (!ValidationUtil.isBlank(email) && userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email da duoc su dung.");
        }

        user.setId(0);
        user.setUserName(username);
        user.setEmail(email);
        user.setFullName(ValidationUtil.trim(user.getFullName()));
        user.setPhone(ValidationUtil.trim(user.getPhone()));
        user.setPassWord(PasswordUtil.hash(user.getPassWord()));
        if (user.getCreatedDate() == null) {
            user.setCreatedDate(new Date());
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        // Nap entity dang quan ly: khong merge truc tiep doi tuong tu form
        // de tranh vo tinh ghi de password/createdDate bang gia tri rong.
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan."));

        validateForUpdate(user);

        String username = ValidationUtil.trim(user.getUserName());
        String email = ValidationUtil.trim(user.getEmail());

        userRepository.findByUserNameIgnoreCase(username)
                .filter(found -> found.getId() != user.getId())
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Ten dang nhap da ton tai.");
                });
        if (!ValidationUtil.isBlank(email)) {
            userRepository.findByEmailIgnoreCase(email)
                    .filter(found -> found.getId() != user.getId())
                    .ifPresent(found -> {
                        throw new IllegalArgumentException("Email da duoc su dung.");
                    });
        }

        existing.setUserName(username);
        existing.setEmail(email);
        existing.setFullName(ValidationUtil.trim(user.getFullName()));
        existing.setPhone(ValidationUtil.trim(user.getPhone()));
        existing.setRoleid(user.getRoleid());
        existing.setActive(user.getActive());

        // Mat khau chi doi khi admin nhap gia tri moi.
        if (!ValidationUtil.isBlank(user.getPassWord())) {
            existing.setPassWord(PasswordUtil.hash(user.getPassWord()));
        }
        return userRepository.save(existing);
    }

    @Override
    public void delete(int id) throws Exception {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan."));

        // Chan xoa tai khoan admin cuoi cung de khong tu khoa quyen quan tri.
        if (existing.getRoleid() == User.ROLE_ADMIN
                && userRepository.countByRoleid(User.ROLE_ADMIN) <= 1) {
            throw new IllegalArgumentException("Khong the xoa tai khoan quan tri vien cuoi cung.");
        }
        userRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    @Override
    public void updatePassword(int id, String rawPassword) {
        if (ValidationUtil.isBlank(rawPassword) || rawPassword.length() < 6) {
            throw new IllegalArgumentException("Mat khau phai co it nhat 6 ky tu.");
        }
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan."));
        existing.setPassWord(PasswordUtil.hash(rawPassword));
        userRepository.save(existing);
    }

    private void validateForInsert(User user) {
        ValidationUtil.requireLength(user.getUserName(), "Ten dang nhap", 1, 100);
        if (ValidationUtil.containsWhitespace(user.getUserName())) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc chua khoang trang.");
        }
        if (!ValidationUtil.isBlank(user.getEmail())) {
            ValidationUtil.requireEmail(user.getEmail());
        }
        ValidationUtil.requireLength(user.getFullName(), "Ho va ten", 1, 255);
        ValidationUtil.requirePhone(user.getPhone());
        if (ValidationUtil.isBlank(user.getPassWord()) || user.getPassWord().length() < 6) {
            throw new IllegalArgumentException("Mat khau phai co it nhat 6 ky tu.");
        }
        requireValidRoleAndActive(user);
    }

    private void validateForUpdate(User user) {
        ValidationUtil.requireId(String.valueOf(user.getId()), "Ma tai khoan");
        ValidationUtil.requireLength(user.getUserName(), "Ten dang nhap", 1, 100);
        if (ValidationUtil.containsWhitespace(user.getUserName())) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc chua khoang trang.");
        }
        if (!ValidationUtil.isBlank(user.getEmail())) {
            ValidationUtil.requireEmail(user.getEmail());
        }
        ValidationUtil.requireLength(user.getFullName(), "Ho va ten", 1, 255);
        ValidationUtil.requirePhone(user.getPhone());
        requireValidRoleAndActive(user);
    }

    private void requireValidRoleAndActive(User user) {
        if (user.getRoleid() != User.ROLE_ADMIN && user.getRoleid() != User.ROLE_USER) {
            throw new IllegalArgumentException("Vai tro khong hop le.");
        }
        if (user.getActive() != 0 && user.getActive() != 1) {
            throw new IllegalArgumentException("Trang thai hoat dong khong hop le.");
        }
    }
}
