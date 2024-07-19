package it.unisalento.rec.rec_energyresources.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_energyresources.dto.ResourceDTO;
import it.unisalento.rec.rec_energyresources.dto.ResourceRequestDTO;
import it.unisalento.rec.rec_energyresources.exceptions.ResourceNotFoundException;
import it.unisalento.rec.rec_energyresources.security.CheckJwt;
import it.unisalento.rec.rec_energyresources.service.ResourceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    @Autowired
    ResourceDetailsService resourceDetailsService;
    @Autowired
    CheckJwt checkJwt;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody ResourceDTO resourceDTO) throws JsonProcessingException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.createResource(resourceDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //recupera le istanze di energia a disposizione inserita da un certo membro
    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws ResourceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.searchResourceByMemberEmail(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> readOne(@RequestHeader("Authorization") String token, @PathVariable String id) throws ResourceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token,ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.searchById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    //recupera le istanze di energia che sono compatibili con un certo task
    @RequestMapping(value = "/searchAllforTask", method = RequestMethod.POST)
    public ResponseEntity<?> readForTask(@RequestHeader("Authorization") String token, @RequestBody ResourceRequestDTO resourceRequestDTO) throws ResourceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.searchResourceForTask(resourceRequestDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id) throws ResourceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.deleteResource(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll(@RequestHeader("Authorization") String token, @RequestParam String memberEmail) throws ResourceNotFoundException {
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.deleteAllResource(memberEmail));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody ResourceDTO resourceDTO) throws ResourceNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("MEMBER");
        ruoliautorizzati.add("ADMIN");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.updateResource(id, resourceDTO));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }

    @RequestMapping(value = "/updateTime/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update_availableTime(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestParam int executionTime) throws ResourceNotFoundException{
        ArrayList<String> ruoliautorizzati = new ArrayList<>();
        ruoliautorizzati.add("CLIENT");
        ResponseEntity<?> response = checkJwt.check(token, ruoliautorizzati);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(resourceDetailsService.updateAvailableTime(id, executionTime));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
    }
}
