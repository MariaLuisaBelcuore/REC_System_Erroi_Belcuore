package it.unisalento.rec.rec_payment.restcontrollers;

import it.unisalento.rec.rec_payment.dto.ClientPaymentDTO;
import it.unisalento.rec.rec_payment.exceptions.PaymentNotFoundException;
import it.unisalento.rec.rec_payment.security.CheckJwt;
import it.unisalento.rec.rec_payment.service.ClientPaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment/client")
public class ClientPaymentController {
    @Autowired
    ClientPaymentDetailsService clientPaymentDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody ClientPaymentDTO clientPaymentDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(clientPaymentDetailsService.createClientPayment(clientPaymentDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> readAll(@RequestHeader("Authorization") String token) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(clientPaymentDetailsService.searchAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAllByClient", method = RequestMethod.GET)
    public ResponseEntity<?> readAllOfClient(@RequestHeader("Authorization") String token, @RequestParam String clientEmail) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(clientPaymentDetailsService.searchClientPaymentByClientEmail(clientEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @PathVariable String id) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(clientPaymentDetailsService.searchClientPaymentById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(clientPaymentDetailsService.deleteClientPayment(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
