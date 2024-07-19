package it.unisalento.rec.rec_email.restcontrollers;

import it.unisalento.rec.rec_email.service.TemporaryCodeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/verificationCode")
public class TemporaryCodeController {
    @Autowired
    TemporaryCodeDetailsService temporaryCodeDetailsService;

    @RequestMapping(value="/{code}", method = RequestMethod.GET)
    public Boolean verification(@PathVariable String code, @RequestParam String email) {
        return temporaryCodeDetailsService.validateCode(code, email);
    }
}
