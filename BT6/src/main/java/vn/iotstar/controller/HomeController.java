package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** BT6 chi co chuc nang Category nen trang goc chuyen thang toi danh sach. */
    @GetMapping("/")
    public String home() {
        return "redirect:/admin/categories/searchpaginated";
    }
}
