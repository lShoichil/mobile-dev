package edu.mobiledev.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    @Schema(
        description = "Id пользователя",
        example = "1"
    )
    private Long userId;

    @Schema(
        description = "Роль пользователя",
        example = "USER"
    )
    private String role;

    @Schema(
        description = "Access token пользователя",
        example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                  "eyJzdWIiOiI1IiwiaXNzIjoiTXlBcHAiLCJleHAiOjE2NzA5MzkyNDYsImlhdCI6MTY3MDkzODM0Nn0." +
                  "mrjtl6L2C_G1nwaD3gs0B_QgaUAGzLjDCUQO9UygGispa55scIC5BhAdRHWSVpklIjEO9iaHlGjVvUjnzEebrg"
    )
    private String accessToken;

    @Schema(
        description = "Refresh token пользователя",
        example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
                  ".eyJzdWIiOiI1IiwiaXNzIjoiTXlBcHAiLCJleHAiOjE2NzA5MzkyNDYsImlhdCI6MTY3MDkzODM0Nn0" +
                  ".mrjtl6L2C_G1nwaD3gs0B_QgaUAGzLjDCUQO9UygGispa55scIC5BhAdRHWSVpklIjEO9iaHlGjVvUjnzEebrg"
    )
    private String refreshToken;

}
