package vn.iotstar.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Bat loi upload vuot gioi han, hien thong bao than thien thay vi trang 413 mac dinh.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException e, RedirectAttributes redirect) {
        redirect.addFlashAttribute("error", "File upload vượt quá giới hạn cho phép (tối đa 5 MB).");
        return "redirect:/admin/categories/searchpaginated";
    }
}
