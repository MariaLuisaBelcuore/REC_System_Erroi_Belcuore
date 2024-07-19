package it.unisalento.rec.rec_performance.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_performance.exceptions.PerformanceNotFoundException;
import it.unisalento.rec.rec_performance.security.CheckJwt;
import it.unisalento.rec.rec_performance.service.PerformanceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {
    @Autowired
    PerformanceDetailsService performanceDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public ResponseEntity<?> calculate(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceDetailsService.calculate());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/reportOfPeriod", method = RequestMethod.GET)
    public ResponseEntity<?> report(@RequestHeader("Authorization") String token, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime calculationDateStart, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime calculationDateEnd) throws PerformanceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceDetailsService.report(calculationDateStart, calculationDateEnd));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token) throws PerformanceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(performanceDetailsService.searchAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
