package it.unisalento.rec.rec_reward.restcontrollers;

import it.unisalento.rec.rec_reward.dto.RewardDTO;
import it.unisalento.rec.rec_reward.exceptions.RewardNotFoundException;
import it.unisalento.rec.rec_reward.security.CheckJwt;
import it.unisalento.rec.rec_reward.service.RewardDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/reward")
public class RewardController {
    @Autowired
    RewardDetailsService rewardDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody RewardDTO rewardDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardDetailsService.createReward(rewardDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value="/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> search(@RequestHeader("Authorization") String token, @PathVariable String id) throws RewardNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(rewardDetailsService.search(id), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    // Recupera tutti i reward che corrispondono al criterio di ricerca (se presente) altrimenti li recupera tutti
    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> searchAll(@RequestHeader("Authorization") String token, @RequestParam(value = "searchTerm", required = false) String searchTerm) throws RewardNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {

            if (searchTerm != null && !searchTerm.isEmpty()) {
                return ResponseEntity.ok(rewardDetailsService.searchByCriteria(searchTerm));
            }
            return ResponseEntity.ok(rewardDetailsService.searchAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) throws RewardNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardDetailsService.deleteReward(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody RewardDTO rewardDTO) throws RewardNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardDetailsService.updateReward(id, rewardDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
