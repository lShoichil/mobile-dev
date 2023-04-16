package edu.mobiledev.exception;

import java.util.*;

import edu.mobiledev.dto.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(NotFoundException exception) {
        log.error("Произошла ошибка нахождения объекта в БД: {}",exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ApiExceptionDto(
            exception.getMessage(),
            exception.getTimestamp()
        ));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(JwtException exception) {
        log.error("Произошла ошибка авторизации: {}",exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ApiExceptionDto(
            exception.getMessage(),
            exception.getTimestamp()
        ));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(AlreadyExistsException exception) {
        log.error("Произошёл конфликт: {}",exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ApiExceptionDto(
            exception.getMessage(),
            exception.getTimestamp()
        ));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MediaUploadFailedException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(MediaUploadFailedException exception) {
        log.error("Произошла ошибка сервера: {}",exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ApiExceptionDto(
            exception.getMessage(),
            exception.getTimestamp()
        ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiExceptionDto> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.error("Произошла попытка добавить слишком большой по объёму файл");
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ApiExceptionDto(
            "Файл слишком большой",
            null
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        log.error("Входная сущность/параметр не прошла(-и) валидацию");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
