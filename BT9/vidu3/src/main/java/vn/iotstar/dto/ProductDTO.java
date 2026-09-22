package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Ten san pham khong duoc de trong")
    private String name;

    private String description;

    @NotNull(message = "Gia khong duoc de trong")
    @DecimalMin(value = "0.0", message = "Gia phai >= 0")
    private BigDecimal price;

    private String imageUrl;
    private Long userId;
    private String username;

    private MultipartFile image;
}
