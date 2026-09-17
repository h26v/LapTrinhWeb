package vn.iotstar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(Long productId,
                              String productName,
                              String images,
                              BigDecimal unitPrice,
                              BigDecimal discount,
                              String description,
                              Long categoryId,
                              String categoryName,
                              Integer quantity,
                              Short status,
                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createDate,
                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime updateDate) {
}
