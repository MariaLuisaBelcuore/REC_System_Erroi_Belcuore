package it.unisalento.rec.rec_reward.restcontrollers;

import it.unisalento.rec.rec_reward.domain.ExceptionResponse;
import it.unisalento.rec.rec_reward.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_reward.exceptions.RewardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionsController {

    @ExceptionHandler(RewardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(RewardNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "The element is not present in the database");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotPermittedException(OperationNotPermittedException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), "Operation not permitted");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
