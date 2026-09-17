package vn.iotstar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Tên category không được để trống")
        @Size(max = 100, message = "Tên category tối đa 100 ký tự")
        String categoryName,

        @Size(max = 500, message = "Đường dẫn icon tối đa 500 ký tự")
        String icon) {
}
