package edu.mobiledev.exception;

import java.time.*;

import lombok.*;
import org.springframework.http.*;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final ZonedDateTime timestamp;

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Z"));
    }

    public ApiException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Z"));
    }

}
