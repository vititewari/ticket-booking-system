package com.example.ticket_booking_system;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key=Keys.hmacShaKeyFor(
            "mySecretKey12345mySecretKey12345".getBytes()
    );
    private final long EXPIRATION=1000*60*60*24;
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(key)
                .compact();
    }
    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
    public boolean isTokenValid(String token){
        try{
            extractUsername(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}