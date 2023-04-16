package edu.mobiledev.exception;

import org.springframework.http.*;

public class JwtException extends ApiException {

    public JwtException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public JwtException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }

}
