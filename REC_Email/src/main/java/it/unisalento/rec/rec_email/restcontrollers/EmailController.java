package it.unisalento.rec.rec_email.restcontrollers;

import it.unisalento.rec.rec_email.domain.Email;
import it.unisalento.rec.rec_email.domain.TemporaryCode;
import it.unisalento.rec.rec_email.dto.EmailDTO;
import it.unisalento.rec.rec_email.exceptions.EmailNotFoundException;
import it.unisalento.rec.rec_email.security.CheckJwt;
import it.unisalento.rec.rec_email.service.EmailDetailsService;
import it.unisalento.rec.rec_email.service.TemporaryCodeDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    EmailDetailsService emailDetailsService;
    @Autowired
    TemporaryCodeDetailsService temporaryCodeDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value="/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmail(@RequestHeader("Authorization") String token, @RequestBody @Valid EmailDTO emailDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            Email email = emailDetailsService.createEmail(emailDTO);
            emailDetailsService.sendEmail(email);
            return new ResponseEntity<>(email, HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value="/sendInvitation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendInvitationAdmin(@RequestHeader("Authorization") String token, @RequestBody @Valid EmailDTO emailDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            Email email = emailDetailsService.createEmail(emailDTO);
            TemporaryCode tempCode = temporaryCodeDetailsService.generateCode(email.getEmailTo());
            email.setSubject("INVITATION ADMINISTRATION");
            email.setText(
                    "Dear " + emailDTO.getOwnerRef() + ",\n\n" +
                            "We are pleased to invite you to take part in our administration. \n\n" +
                            "Please register with your email \" " + email.getEmailTo() + " \" by providing the following code: \n" +
                            tempCode.getCode() + "\n\n" +
                            "If you have any questions or need further assistance, " +
                            "please feel free to contact us.\n\n" +
                            "Sincerely,\n" +
                            "The RecSystem team"
            );
            emailDetailsService.sendEmail(email);
            return new ResponseEntity<>("Invitation sent to " + email.getEmailTo(), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value="/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> searchAll(@RequestHeader("Authorization") String token) throws EmailNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(emailDetailsService.searchAll(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //cerca tutte le email inviate ad un certo utente
    @RequestMapping(value="/searchAllEmailTo", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllEmailTo(@RequestHeader("Authorization") String token, @RequestParam String emailTo) throws EmailNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(emailDetailsService.searchAllEmailTo(emailTo), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value="/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> search(@RequestHeader("Authorization") String token, @PathVariable String id) throws EmailNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(emailDetailsService.search(id), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) throws EmailNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(emailDetailsService.deleteEmail(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //cancella tutte le email inviate ad un certo utente
    @RequestMapping(value = "/deleteAllEmailTo", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll(@RequestHeader("Authorization") String token, @RequestParam String emailTo) throws EmailNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(emailDetailsService.deleteAllEmailTo(emailTo));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
