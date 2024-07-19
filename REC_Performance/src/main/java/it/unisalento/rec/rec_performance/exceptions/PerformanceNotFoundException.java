package it.unisalento.rec.rec_performance.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PerformanceNotFoundException extends RuntimeException{
    public PerformanceNotFoundException(String message) {
        super(message);
    }
}

