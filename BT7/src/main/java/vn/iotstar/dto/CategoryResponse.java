package vn.iotstar.dto;

public record CategoryResponse(Long categoryId, String categoryName, String icon, long productCount) {
}
