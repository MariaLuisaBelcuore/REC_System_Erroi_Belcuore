package it.unisalento.rec.rec_performance.restcontrollers;

import it.unisalento.rec.rec_performance.domain.ExceptionResponse;
import it.unisalento.rec.rec_performance.exceptions.PerformanceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(PerformanceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(PerformanceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "The element is not present in the database");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
