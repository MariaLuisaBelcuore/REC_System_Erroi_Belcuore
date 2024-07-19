package it.unisalento.rec.rec_email.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class OperationNotPermittedException extends RuntimeException{
    public OperationNotPermittedException(String message) {super(message);}
}
