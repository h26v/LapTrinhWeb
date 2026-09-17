package vn.iotstar.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Tên product không được để trống")
        @Size(max = 150, message = "Tên product tối đa 150 ký tự")
        String productName,

        @Size(max = 500, message = "Đường dẫn hình ảnh tối đa 500 ký tự")
        String images,

        @NotNull(message = "Đơn giá là bắt buộc")
        @DecimalMin(value = "0.0", inclusive = true, message = "Đơn giá không hợp lệ")
        BigDecimal unitPrice,

        @NotNull(message = "Giảm giá là bắt buộc")
        @DecimalMin(value = "0.0", inclusive = true, message = "Giảm giá từ 0 đến 100")
        @DecimalMax(value = "100.0", inclusive = true, message = "Giảm giá từ 0 đến 100")
        BigDecimal discount,

        @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
        String description,

        @NotNull(message = "Category là bắt buộc")
        Long categoryId,

        @NotNull(message = "Số lượng là bắt buộc")
        @Min(value = 0, message = "Số lượng không được âm")
        Integer quantity,

        @NotNull(message = "Trạng thái là bắt buộc")
        @Min(value = 0, message = "Trạng thái không hợp lệ")
        @Max(value = 1, message = "Trạng thái không hợp lệ")
        Short status) {
}
