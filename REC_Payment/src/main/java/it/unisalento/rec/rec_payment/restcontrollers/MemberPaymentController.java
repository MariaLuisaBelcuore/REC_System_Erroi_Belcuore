package it.unisalento.rec.rec_payment.restcontrollers;

import it.unisalento.rec.rec_payment.dto.MemberPaymentDTO;
import it.unisalento.rec.rec_payment.exceptions.PaymentNotFoundException;
import it.unisalento.rec.rec_payment.security.CheckJwt;
import it.unisalento.rec.rec_payment.service.MemberPaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment/member")
public class MemberPaymentController {
    @Autowired
    MemberPaymentDetailsService memberPaymentDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/createPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody MemberPaymentDTO memberPaymentDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(memberPaymentDetailsService.createMemberPayment(memberPaymentDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> readAll(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(memberPaymentDetailsService.searchMemberPaymentByMemberEmail(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @PathVariable String id) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(memberPaymentDetailsService.searchMemberPaymentById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) throws PaymentNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(memberPaymentDetailsService.deleteMemberPayment(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
