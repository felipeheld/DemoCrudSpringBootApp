package com.felipeheld.grades.api.controller.advice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.felipeheld.grades.api.response.error.ApiError;
import com.felipeheld.grades.exception.NotaAlreadyExistsException;
import com.felipeheld.grades.exception.NotaDoesNotExistException;
import com.felipeheld.grades.exception.RepositoryException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
     
    @ExceptionHandler(RepositoryException.class)
    protected ResponseEntity<ApiError> handleRepositoryException(RepositoryException exception) {
        var error = ApiError.builder().message("An error occurred in our server.").timestamp(LocalDateTime.now()).build();

        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { NotaAlreadyExistsException.class, NotaDoesNotExistException.class })
    protected ResponseEntity<ApiError> handleNotaAlreadyExistsOrNotaDoesNotExistException(Exception exception) {
        var error = ApiError.builder().message(exception.getMessage()).timestamp(LocalDateTime.now()).build();

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, 
        HttpHeaders headers, 
        HttpStatus status, 
        WebRequest request) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList());
        
        var error = ApiError.builder().message(String.join(" ", errors)).timestamp(LocalDateTime.now()).build();

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
