package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username khong duoc de trong")
    private String username;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    private String email;

    @NotBlank(message = "Ho ten khong duoc de trong")
    private String fullName;

    private boolean enabled;
    private String roleName;
    private long productCount;
}
