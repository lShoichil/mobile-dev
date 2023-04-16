package edu.mobiledev.dto;

import java.time.*;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Schema(description = "Сообщение об ошибке")
public class ApiExceptionDto {

    @Schema(description = "Описание ошибке")
    private String message;

    @Schema(defaultValue = "Время возникновения ошибки")
    private ZonedDateTime timestamp;

}
