package it.unisalento.rec.rec_task.restcontrollers;

import it.unisalento.rec.rec_task.domain.ExceptionResponse;
import it.unisalento.rec.rec_task.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_task.exceptions.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(TaskNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "The element is not present in the database");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotPermittedException(OperationNotPermittedException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "Operation not permitted");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
