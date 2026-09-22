package vn.iotstar.mapper;

import org.mapstruct.*;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "image", ignore = true)
    ProductDTO toDTO(Product entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductDTO dto);
}
