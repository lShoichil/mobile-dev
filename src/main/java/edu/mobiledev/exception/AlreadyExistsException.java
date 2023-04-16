package edu.mobiledev.exception;

import org.springframework.http.*;

public class AlreadyExistsException extends ApiException {

    public AlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(HttpStatus.CONFLICT, message, cause);
    }

}
