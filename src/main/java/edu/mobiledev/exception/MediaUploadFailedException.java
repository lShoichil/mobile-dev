package edu.mobiledev.exception;

import org.springframework.http.*;

public class MediaUploadFailedException extends ApiException {

    public MediaUploadFailedException(String message) {
        super(HttpStatus.EXPECTATION_FAILED, message);
    }

    public MediaUploadFailedException(String message, Throwable cause) {
        super(HttpStatus.EXPECTATION_FAILED, message, cause);
    }

}
