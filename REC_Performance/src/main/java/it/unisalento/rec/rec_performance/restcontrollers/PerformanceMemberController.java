package it.unisalento.rec.rec_performance.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_performance.exceptions.PerformanceNotFoundException;
import it.unisalento.rec.rec_performance.security.CheckJwt;
import it.unisalento.rec.rec_performance.service.PerformanceMemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/performance/member")
public class PerformanceMemberController {
    @Autowired
    PerformanceMemberDetailsService performanceMemberDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public ResponseEntity<?> calculate(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceMemberDetailsService.calculate(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/reportOfPeriod", method = RequestMethod.GET)
    public ResponseEntity<?> report(@RequestHeader("Authorization") String token, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime calculationDateStart, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime calculationDateEnd, @RequestParam String memberEmail) throws PerformanceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceMemberDetailsService.report(calculationDateStart, calculationDateEnd, memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws PerformanceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceMemberDetailsService.searchAllByMember(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
