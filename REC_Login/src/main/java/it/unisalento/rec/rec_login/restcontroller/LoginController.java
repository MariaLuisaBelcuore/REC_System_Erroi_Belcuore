package it.unisalento.rec.rec_login.restcontroller;

import it.unisalento.rec.rec_login.domain.Admin;
import it.unisalento.rec.rec_login.domain.Client;
import it.unisalento.rec.rec_login.domain.Member;
import it.unisalento.rec.rec_login.dto.*;
import it.unisalento.rec.rec_login.exceptions.UserNotFoundException;
import it.unisalento.rec.rec_login.repositories.AdminRepository;
import it.unisalento.rec.rec_login.repositories.ClientRepository;
import it.unisalento.rec.rec_login.repositories.MemberRepository;
import it.unisalento.rec.rec_login.security.JwtUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilities jwtUtilities;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDTO userloginDTO) throws UserNotFoundException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userloginDTO.getEmail(),
                        userloginDTO.getPassword()
                )
        );

        Client client = clientRepository.findClientByEmail(authentication.getName());
        if (client == null) {
            Member member = memberRepository.findMemberByEmail(authentication.getName());

            if(member == null){
                Admin admin = adminRepository.findAdminByEmail(authentication.getName());

                if(admin==null) {
                    throw new UserNotFoundException("User with email " + authentication.getName() + " not found");
                }
                String role = "ADMIN";
                SecurityContextHolder.getContext().setAuthentication(authentication);
                final String jwt = jwtUtilities.generateToken(admin.getEmail(), role);
                return ResponseEntity.ok(new LoginResponseDTO(jwt));
            }
            String role = "MEMBER";
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String jwt = jwtUtilities.generateToken(member.getEmail(), role);
            return ResponseEntity.ok(new LoginResponseDTO(jwt));
        }
        String role = "CLIENT";
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtilities.generateToken(client.getEmail(), role);
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }
}
