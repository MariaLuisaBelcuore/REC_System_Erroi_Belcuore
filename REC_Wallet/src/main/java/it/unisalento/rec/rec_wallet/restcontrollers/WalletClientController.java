package it.unisalento.rec.rec_wallet.restcontrollers;

import it.unisalento.rec.rec_wallet.dto.WalletClientDTO;
import it.unisalento.rec.rec_wallet.exceptions.WalletNotFoundException;
import it.unisalento.rec.rec_wallet.security.CheckJwt;
import it.unisalento.rec.rec_wallet.service.WalletClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/wallet/client")
public class WalletClientController {
    @Autowired
    WalletClientDetailsService walletClientDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody WalletClientDTO walletClientDTO) {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(walletClientDetailsService.createWalletClient(walletClientDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @RequestParam String clientEmail) throws WalletNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(walletClientDetailsService.searchWalletClientByClientEmail(clientEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> readAll(@RequestHeader("Authorization") String token) throws WalletNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(walletClientDetailsService.searchAllWalletClient());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestParam String clientEmail) throws WalletNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(walletClientDetailsService.deleteWalletClient(clientEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @RequestBody WalletClientDTO walletClientDTO) throws WalletNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(walletClientDetailsService.updateWalletClient(walletClientDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
