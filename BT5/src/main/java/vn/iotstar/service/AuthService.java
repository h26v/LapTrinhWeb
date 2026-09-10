package vn.iotstar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.util.PasswordUtil;
import vn.iotstar.util.ValidationUtil;

/**
 * Xu ly dang nhap. Tai khoan demo cu luu mat khau plain text se duoc
 * tu dong nang cap sang BCrypt ngay sau lan dang nhap thanh cong dau tien.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @return User neu dang nhap thanh cong, null neu that bai.
     */
    @Transactional
    public User login(String username, String password) {
        if (ValidationUtil.isBlank(username) || password == null || username.trim().length() > 100) {
            return null;
        }

        User user = userRepository.findByUserNameIgnoreCase(username.trim()).orElse(null);
        if (user == null || user.getActive() != 1) {
            return null;
        }
        if (!PasswordUtil.matches(password, user.getPassWord())) {
            return null;
        }

        // Nang cap mat khau plain text cu len BCrypt.
        if (!PasswordUtil.isBCrypt(user.getPassWord())) {
            user.setPassWord(PasswordUtil.hash(password));
            user = userRepository.save(user);
        }
        return user;
    }
}
