package vn.iotstar.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;
import vn.iotstar.util.ValidationUtil;

@Controller
public class ProductWebController {

    private static final int PAGE_SIZE = 6;

    private final ProductService productService;

    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int currentPage = Math.max(1, page);
        Pageable pageable = PageRequest.of(currentPage - 1, PAGE_SIZE);

        Page<Product> result = productService.findActive(pageable);
        model.addAttribute("products", result.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("total", productService.countActive());
        return "web/product-list";
    }

    @GetMapping("/product/detail")
    public String detail(@RequestParam(value = "id", required = false) String id, Model model) {
        int productId = ValidationUtil.parseId(id);
        Product product = productId > 0 ? productService.findById(productId) : null;
        if (product == null || product.getStatus() != 1) {
            return "redirect:/product";
        }
        model.addAttribute("product", product);
        return "web/product-detail";
    }
}
