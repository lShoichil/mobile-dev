package edu.mobiledev.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAddRequestDto {

    @Schema(
        description = "Id пользователя",
        example = "1"
    )
    private Long userId;

    @Schema(
        description = "Id избранного",
        example = "1"
    )
    private Long favoriteId;

}
