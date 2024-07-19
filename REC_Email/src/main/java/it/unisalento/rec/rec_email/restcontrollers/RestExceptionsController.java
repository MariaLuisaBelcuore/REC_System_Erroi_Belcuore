package it.unisalento.rec.rec_email.restcontrollers;

import it.unisalento.rec.rec_email.domain.ExceptionResponse;
import it.unisalento.rec.rec_email.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_email.exceptions.EmailNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEmailNotFoundException(EmailNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "The element is not present in the database");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotPermittedException(OperationNotPermittedException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "Operation not permitted");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
