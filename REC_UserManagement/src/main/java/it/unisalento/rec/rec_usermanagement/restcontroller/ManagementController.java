package it.unisalento.rec.rec_usermanagement.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.dto.UserDTO;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_usermanagement.exceptions.UserNotFoundException;
import it.unisalento.rec.rec_usermanagement.security.CheckJwt;
import it.unisalento.rec.rec_usermanagement.service.ManagementDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/management")
public class ManagementController {
    @Autowired
    ManagementDetailsService managementDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/delete/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String email) throws UserNotFoundException, JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.deleteUser(email));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/deleteForAdmin/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteForAdmin(@RequestHeader("Authorization") String token, @PathVariable String email) throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.deleteForAdmin(email));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody UserDTO userDTO) throws OperationNotPermittedException, UserNotFoundException, JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.updateUser(id, userDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/updateForAdmin/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateForAdmin(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody UserDTO userDTO, @RequestParam String email) throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.updateForAdmin(id, userDTO, email));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll/client", method = RequestMethod.GET)
    public ResponseEntity<?> readClient(@RequestHeader("Authorization") String token) throws UserNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.searchAllClient());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll/member", method = RequestMethod.GET)
    public ResponseEntity<?> readMember(@RequestHeader("Authorization") String token) throws UserNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.searchAllMember());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/searchAll/admin", method = RequestMethod.GET)
    public ResponseEntity<?> readAdmin(@RequestHeader("Authorization") String token) throws UserNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.searchAllAdmin());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/search/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @PathVariable String email) throws UserNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("ADMIN");
        ruoliautorizzati.add("CLIENT");
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(managementDetailsService.searchByEmail(email));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }


}
