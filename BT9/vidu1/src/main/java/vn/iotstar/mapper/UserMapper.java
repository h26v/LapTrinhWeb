package vn.iotstar.mapper;

import org.mapstruct.*;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    UserDTO toDto(User entity);

    @Mapping(target = "role", ignore = true)
    User toEntity(UserDTO dto);
}
