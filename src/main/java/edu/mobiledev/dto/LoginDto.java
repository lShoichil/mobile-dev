package edu.mobiledev.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @Schema(
        description = "Логин пользователя",
        example = "imorozov"
    )
    private String login;

    @Schema(
        description = "Пароль пользователя",
        example = "imorozov"
    )
    private String password;

}
