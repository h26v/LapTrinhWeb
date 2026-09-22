package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageController {

    @GetMapping
    public String error(Model model) {
        model.addAttribute("message", "Da xay ra loi.");
        return "error";
    }
}
