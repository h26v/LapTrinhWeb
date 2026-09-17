package vn.iotstar.controller;

import vn.iotstar.dto.ApiResponse;
import vn.iotstar.dto.CategoryRequest;
import vn.iotstar.dto.CategoryResponse;
import vn.iotstar.dto.PageResponse;
import vn.iotstar.service.CategoryService;
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

@RestController
@Validated
@RequestMapping({"/api/category", "/api/categories"})
public class CategoryApiController {

    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    public CategoryApiController(CategoryService categoryService, FileStorageService fileStorageService) {
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ApiResponse<PageResponse<CategoryResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page phải từ 0") int page,
            @RequestParam(defaultValue = "8") @Min(value = 1, message = "Size phải lớn hơn 0")
            @Max(value = 50, message = "Size tối đa 50") int size,
            @RequestParam(defaultValue = "") String search) {
        return ApiResponse.ok("Lấy danh sách category thành công", categoryService.search(search, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok("Lấy category thành công", categoryService.getById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thêm category thành công", categoryService.create(request)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> createMultipart(
            @RequestParam String categoryName,
            @RequestPart(value = "icon", required = false) MultipartFile icon) {
        String iconUrl = fileStorageService.store(icon);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thêm category thành công",
                        categoryService.create(new CategoryRequest(categoryName, iconUrl))));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<CategoryResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok("Cập nhật category thành công", categoryService.update(id, request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryResponse> updateMultipart(
            @PathVariable Long id,
            @RequestParam String categoryName,
            @RequestPart(value = "icon", required = false) MultipartFile icon) {
        CategoryResponse current = categoryService.getById(id);
        String iconUrl = icon == null || icon.isEmpty() ? current.icon() : fileStorageService.store(icon);
        return ApiResponse.ok("Cập nhật category thành công",
                categoryService.update(id, new CategoryRequest(categoryName, iconUrl)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.ok("Xóa category thành công", null);
    }
}
