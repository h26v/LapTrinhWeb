package vn.iotstar.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Bat loi upload vuot gioi hien thi thong bao than thien thay vi trang 413 mac dinh.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException e,
            HttpServletRequest request, RedirectAttributes redirect) {

        redirect.addFlashAttribute("error", "File upload vuot qua gioi han cho phep (toi da 5 MB).");

        // Quay ve dung form nguoi dung vua gui thay vi danh sach chung.
        String uri = request.getRequestURI();
        if (uri != null && uri.contains("/category/")) {
            return "redirect:/admin/categories";
        }
        if (uri != null && uri.contains("/user/")) {
            return "redirect:/admin/users";
        }
        return "redirect:/admin/products";
    }
}
