package it.unisalento.rec.rec_reward.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RewardNotFoundException extends RuntimeException{
    public RewardNotFoundException(String message) {
        super(message);
    }
}