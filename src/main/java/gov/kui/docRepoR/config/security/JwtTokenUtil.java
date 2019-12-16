package gov.kui.docRepoR.config.security;

import gov.kui.docRepoR.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import static gov.kui.docRepoR.model.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static gov.kui.docRepoR.model.Constants.SIGNING_KEY;

@Component
public class JwtTokenUtil {
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("kui")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY) //todo добавить SecurityService
                .compact();
    }
}
