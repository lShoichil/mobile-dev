package edu.mobiledev.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на запрос пользователя по id")
public class UserResponseDto {

    @Schema(
        description = "id пользователя",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Логин пользователя",
        example = "iIvanov"
    )
    private String login;

    @Schema(
        description = "ФИО пользователя",
        example = "Иванов Иван Иванович"
    )
    private String fullName;

    @Schema(
        description = "id аватара пользователя",
        example = "1"
    )
    private Long mediaId;

    @Schema(
        description = "Телефон пользователя",
        example = "78008887766"
    )
    private String phone;

    @Schema(
        description = "Электронная почта пользователя",
        example = "iivanov@atb.ru"
    )
    private String email;

    @Schema(
        description = "Роль пользователя",
        example = "USER"
    )
    private String role;

}
