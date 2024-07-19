package it.unisalento.rec.rec_task.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.function.Function;

import static it.unisalento.rec.rec_task.security.SecurityConstants.JWT_SECRET;

@Service
public class JwtUtilities {
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }
        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }
        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }
        private Claims extractAllClaims(String token) {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        }
        public String extractRole(String token) {
            return extractClaim(token, claims -> claims.get("role", String.class));
        }
}

