package it.unisalento.rec.rec_energyresources.restcontrollers;

import it.unisalento.rec.rec_energyresources.domain.ExceptionResponse;
import it.unisalento.rec.rec_energyresources.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "Resource not available");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
