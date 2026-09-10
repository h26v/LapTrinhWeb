package vn.iotstar.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.iotstar.config.StudentInfo;

/**
 * Dua thong tin sinh vien vao model cua moi trang de fragment header/footer su dung,
 * khong phai lap lai trong tung controller.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final StudentInfo studentInfo;

    public GlobalModelAttributes(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    @ModelAttribute("student")
    public StudentInfo student() {
        return studentInfo;
    }
}
