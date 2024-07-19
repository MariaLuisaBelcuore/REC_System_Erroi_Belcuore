package it.unisalento.rec.rec_usermanagement.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.dto.AdminRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.ClientRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.MemberRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.service.RegistrationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    RegistrationDetailsService registrationDetailsService;

    @RequestMapping(value = "/admin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminRegistrationDTO post(@RequestBody AdminRegistrationDTO adminRegistrationDTO, @RequestParam String code) throws JsonProcessingException {return registrationDetailsService.createAdmin(adminRegistrationDTO, code);}

    @RequestMapping(value = "/client", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientRegistrationDTO post(@RequestBody ClientRegistrationDTO clientRegistrationDTO) throws JsonProcessingException {return registrationDetailsService.createClient(clientRegistrationDTO);}

    @RequestMapping(value = "/member", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MemberRegistrationDTO post(@RequestBody MemberRegistrationDTO memberRegistrationDTO) throws JsonProcessingException {return registrationDetailsService.createMember(memberRegistrationDTO);}
}

