package project.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName;
            try{
                fieldName = ((FieldError) error).getField();
            } catch (ClassCastException e) {
                if(error.getArguments()[1].toString().equals("id")){
                    fieldName = error.getArguments()[2].toString();
                } else {
                    fieldName = error.getArguments()[1].toString();
                }
            }
            String errorMessage = error.getDefaultMessage();
            if(errorMessage.contains("String")){
                errorMessage = "Некоректний тип";
            }
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
