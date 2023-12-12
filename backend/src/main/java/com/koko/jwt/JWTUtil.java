package com.koko.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = "mySecretKeymySecretKeymySecretKeymySecretKey";

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, List<String> scopes) {
        return issueToken(subject, Map.of("scope", scopes));
    };
    public String issueToken(
         String subject,
         Map<String, Object> claims) {

         return Jwts
                 .builder()
                 .setClaims(claims)
                 .setSubject(subject)
                 .setIssuer("https://koko.com")
                 .setIssuedAt(Date.from(Instant.now()))
                 .setExpiration(
                         Date.from(
                                 Instant.now().plus(15, DAYS)
                         )
                 )
                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                 .compact();
     }

    private Key getSigningKey() {
         return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        java.util.Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }


}
