package vn.iotstar.controller;

import vn.iotstar.dto.ApiResponse;
import vn.iotstar.dto.PageResponse;
import vn.iotstar.dto.ProductRequest;
import vn.iotstar.dto.ProductResponse;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.FileStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@Validated
@RequestMapping({"/api/product", "/api/products"})
public class ProductApiController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    public ProductApiController(ProductService productService, FileStorageService fileStorageService) {
        this.productService = productService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page phải từ 0") int page,
            @RequestParam(defaultValue = "8") @Min(value = 1, message = "Size phải lớn hơn 0")
            @Max(value = 50, message = "Size tối đa 50") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long categoryId) {
        return ApiResponse.ok("Lấy danh sách product thành công",
                productService.search(search, categoryId, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok("Lấy product thành công", productService.getById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thêm product thành công", productService.create(request)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> createMultipart(
            @RequestParam String productName,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam BigDecimal unitPrice,
            @RequestParam(defaultValue = "0") BigDecimal discount,
            @RequestParam(defaultValue = "") String description,
            @RequestParam Long categoryId,
            @RequestParam Integer quantity,
            @RequestParam Short status) {
        ProductRequest request = new ProductRequest(productName, fileStorageService.store(imageFile), unitPrice,
                discount, description, categoryId, quantity, status);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thêm product thành công", productService.create(request)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Cập nhật product thành công", productService.update(id, request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> updateMultipart(
            @PathVariable Long id,
            @RequestParam String productName,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam BigDecimal unitPrice,
            @RequestParam(defaultValue = "0") BigDecimal discount,
            @RequestParam(defaultValue = "") String description,
            @RequestParam Long categoryId,
            @RequestParam Integer quantity,
            @RequestParam Short status) {
        ProductResponse current = productService.getById(id);
        String imageUrl = imageFile == null || imageFile.isEmpty() ? current.images() : fileStorageService.store(imageFile);
        ProductRequest request = new ProductRequest(productName, imageUrl, unitPrice, discount, description,
                categoryId, quantity, status);
        return ApiResponse.ok("Cập nhật product thành công", productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok("Xóa product thành công", null);
    }
}
