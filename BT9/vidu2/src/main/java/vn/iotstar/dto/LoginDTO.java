package vn.iotstar.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Username hoac email khong duoc de trong")
    private String login;

    @NotBlank(message = "Password khong duoc de trong")
    private String password;
}
