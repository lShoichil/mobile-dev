package edu.mobiledev.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на запрос роли по id")
public class RoleResponseDto {

    @Schema(
        description = "Id роли",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Название роли",
        example = "USER"
    )
    private String name;

}
