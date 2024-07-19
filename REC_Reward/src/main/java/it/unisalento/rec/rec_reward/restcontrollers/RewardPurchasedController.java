package it.unisalento.rec.rec_reward.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_reward.dto.RewardPurchasedDTO;
import it.unisalento.rec.rec_reward.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_reward.exceptions.RewardNotFoundException;
import it.unisalento.rec.rec_reward.security.CheckJwt;
import it.unisalento.rec.rec_reward.service.RewardPurchasedDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/reward/purchased")
public class RewardPurchasedController {
    @Autowired
    RewardPurchasedDetailsService rewardPurchasedDetailsService;
    @Autowired
    CheckJwt checkJwt;

    //si chiama quando il membro compra un reward
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRewardPurchased(@RequestHeader("Authorization") String token, @RequestBody RewardPurchasedDTO rewardPurchasedDTO) throws RewardNotFoundException, JsonProcessingException, OperationNotPermittedException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardPurchasedDetailsService.createRewardPurchased(rewardPurchasedDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //cerca e ritorna tutti i reward comprati da un determinato membro che non sono stati usati
    @RequestMapping(value = "/searchPurchased", method = RequestMethod.GET)
    public ResponseEntity<?> searchPurchasedByMemberEmail(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws RewardNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardPurchasedDetailsService.searchPurchasedByMemberEmail(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //si chiama quando il membro vuole utilizzare un reward (ad esempio vuole utilizzare un buono o uno sconto)
    @RequestMapping(value = "/use/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> useRewardPurchased(@RequestHeader("Authorization") String token, @PathVariable String id) throws RewardNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(rewardPurchasedDetailsService.useRewardPurchased(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
