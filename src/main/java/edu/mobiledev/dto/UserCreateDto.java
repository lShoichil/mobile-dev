package edu.mobiledev.dto;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @Schema(description = "Логин пользователя", example = "iIvanov")
    @NotBlank
    private String login;

    @Schema(description = "Пароль пользователя", example = "9iIvanovPassword8")
    @NotBlank
    private String password;

    @Schema(description = "ФИО пользователя", example = "Иванов Иван Иванович")
    @NotBlank
    private String fullName;

    @Schema(description = "Телефон пользователя", example = "8989829304")
    @Pattern(regexp = "^((8)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message
        = "Номер телефона должен иметь один из следующих форматов: " +
          "8989829304")
    private String phone;

    @Schema(description = "Электронная почта пользователя", example = "iivanov@atb.ru")
    @Email
    private String email;

    @Schema(description = "Id роли пользователя", example = "1")
    @JsonProperty(value = "roleId")
    private Long role;

}
