package it.unisalento.rec.rec_task.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class CheckJwt {
    @Autowired
    private JwtUtilities jwtUtilities;

    public ResponseEntity<?> check(String token, ArrayList<String> ruoliAutorizzati){
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
            }

            String jwtToken = token.substring(7);

            Date expirationDate = jwtUtilities.extractExpiration(jwtToken);

            if (expirationDate.before(new Date())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
            }

            String username = jwtUtilities.extractUsername(jwtToken);
            String ruolo = jwtUtilities.extractRole(jwtToken);

            if(!ruoliAutorizzati.contains(ruolo)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
            }
            return ResponseEntity.ok(ruolo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Optional.empty());
        }
    }
}
