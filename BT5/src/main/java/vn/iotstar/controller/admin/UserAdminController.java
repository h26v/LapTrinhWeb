package vn.iotstar.controller.admin;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;
import vn.iotstar.util.ValidationUtil;

/**
 * CRUD User cho admin, kem chuc nang tim kiem theo username / ho ten / email / dien thoai.
 *
 * <p>Yeu cau quyen admin duoc dam bao boi AdminInterceptor tren /admin/**.
 */
@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private static final String SUB_DIR = "avatar";
    private static final int PAGE_SIZE = 5;

    private final UserService userService;
    private final ImageUploadUtil imageUploadUtil;

    public UserAdminController(UserService userService, ImageUploadUtil imageUploadUtil) {
        this.userService = userService;
        this.imageUploadUtil = imageUploadUtil;
    }

    // ===== Danh sach + tim kiem =====

    @GetMapping("/users")
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page, Model model) {

        int currentPage = Math.max(1, page);
        Pageable pageable = PageRequest.of(currentPage - 1, PAGE_SIZE);

        Page<User> result = userService.search(keyword, pageable);
        model.addAttribute("users", result.getContent());
        model.addAttribute("total", userService.count());
        model.addAttribute("resultSize", result.getTotalElements());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("keyword", ValidationUtil.trim(keyword));
        return "admin/user-list";
    }

    // ===== Them =====

    @GetMapping("/user/add")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-add";
    }

    @PostMapping("/user/insert")
    public String insert(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "roleid", required = false) String roleid,
            @RequestParam(value = "active", required = false) String active,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            Model model, RedirectAttributes redirect) {

        User user = new User();
        user.setUserName(ValidationUtil.trim(username));
        user.setEmail(ValidationUtil.trim(email));
        user.setFullName(ValidationUtil.trim(fullname));
        user.setPhone(ValidationUtil.trim(phone));
        user.setPassWord(password);
        user.setRoleid(parseRoleOrDefault(roleid));
        user.setActive(parseActiveOrDefault(active, 1));

        SavedFile upload = null;
        try {
            upload = imageUploadUtil.saveImage(avatar, SUB_DIR);
            if (upload != null) {
                user.setAvatar(upload.relativePath());
            }
            userService.insert(user);
            redirect.addFlashAttribute("success", "Da them tai khoan \"" + user.getUserName() + "\".");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            // Khong hien lai mat khau trong form.
            user.setPassWord(null);
            model.addAttribute("alert", e.getMessage());
            model.addAttribute("user", user);
            return "admin/user-add";
        } catch (IOException e) {
            deleteQuietly(upload);
            user.setPassWord(null);
            model.addAttribute("alert", "Khong the luu anh upload.");
            model.addAttribute("user", user);
            return "admin/user-add";
        }
    }

    // ===== Sua =====

    @GetMapping("/user/edit")
    public String editForm(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {

        int userId = ValidationUtil.parseId(id);
        User user = userId > 0 ? userService.findById(userId) : null;
        if (user == null) {
            redirect.addFlashAttribute("error", "Khong tim thay tai khoan can sua.");
            return "redirect:/admin/users";
        }
        // Khong de lo hash mat khau ra view.
        user.setPassWord(null);
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/user/update")
    public String update(@RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "roleid", required = false) String roleid,
            @RequestParam(value = "active", required = false) String active,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            Model model, RedirectAttributes redirect) {

        User user = new User();
        user.setId(ValidationUtil.parseId(id));
        user.setUserName(ValidationUtil.trim(username));
        user.setEmail(ValidationUtil.trim(email));
        user.setFullName(ValidationUtil.trim(fullname));
        user.setPhone(ValidationUtil.trim(phone));
        user.setPassWord(password);
        user.setRoleid(parseRoleOrDefault(roleid));
        user.setActive(parseActiveOrDefault(active, 1));

        SavedFile upload = null;
        String oldAvatar = null;
        try {
            User old = userService.findById(user.getId());
            if (old == null) {
                redirect.addFlashAttribute("error", "Khong tim thay tai khoan can cap nhat.");
                return "redirect:/admin/users";
            }
            oldAvatar = old.getAvatar();

            upload = imageUploadUtil.saveImage(avatar, SUB_DIR);
            if (upload != null) {
                user.setAvatar(upload.relativePath());
            }

            userService.update(user);
            if (upload != null) {
                imageUploadUtil.deleteLocalFile(oldAvatar);
            }
            redirect.addFlashAttribute("success", "Da cap nhat tai khoan \"" + user.getUserName() + "\".");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException | UploadValidationException e) {
            deleteQuietly(upload);
            user.setPassWord(null);
            user.setAvatar(oldAvatar);
            model.addAttribute("alert", e.getMessage());
            model.addAttribute("user", user);
            return "admin/user-edit";
        } catch (IOException e) {
            deleteQuietly(upload);
            user.setPassWord(null);
            user.setAvatar(oldAvatar);
            model.addAttribute("alert", "Khong the luu anh upload.");
            model.addAttribute("user", user);
            return "admin/user-edit";
        }
    }

    // ===== Xoa =====

    @GetMapping("/user/delete")
    public String delete(@RequestParam(value = "id", required = false) String id, RedirectAttributes redirect) {
        int userId = ValidationUtil.parseId(id);
        if (userId <= 0) {
            redirect.addFlashAttribute("error", "Ma tai khoan khong hop le.");
            return "redirect:/admin/users";
        }
        try {
            User user = userService.findById(userId);
            userService.delete(userId);
            if (user != null) {
                imageUploadUtil.deleteLocalFile(user.getAvatar());
            }
            redirect.addFlashAttribute("success", "Da xoa tai khoan.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Xoa tai khoan that bai.");
        }
        return "redirect:/admin/users";
    }

    private int parseRoleOrDefault(String roleid) {
        int role = ValidationUtil.parseId(roleid);
        if (role == User.ROLE_ADMIN || role == User.ROLE_USER) {
            return role;
        }
        return User.ROLE_USER;
    }

    private int parseActiveOrDefault(String active, int fallback) {
        if ("0".equals(active)) {
            return 0;
        }
        if ("1".equals(active)) {
            return 1;
        }
        return fallback;
    }

    private void deleteQuietly(SavedFile upload) {
        if (upload == null) {
            return;
        }
        try {
            Path path = upload.file();
            imageUploadUtil.deleteIfExists(path);
        } catch (IOException ignored) {
            // Bo qua loi don file tam.
        }
    }
}
