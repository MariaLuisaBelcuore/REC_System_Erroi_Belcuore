package it.unisalento.rec.rec_login.restcontroller;

import it.unisalento.rec.rec_login.domain.ExceptionResponse;
import it.unisalento.rec.rec_login.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "The element is not present in the database");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
