package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotNull
    private Long roleId;

    private String roleName;
    private boolean enabled;
    private LocalDateTime createdAt;
}
