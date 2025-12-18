package com.example.convospherebackend.services;

import com.example.convospherebackend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecretKey;

    @Value("${app.expiration-ms}")
    private long jwtExpirationms;

    private SecretKey getJwtSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("type","Access")
                .signWith(getJwtSecretKey())
                .issuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .compact();
    }

    public String generateRefreshToken(User user){
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("type","Refresh")
                .signWith(getJwtSecretKey())
                .issuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationms*30*6))
                .compact();
    }

    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getJwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if(!"Access".equals(claims.get("type"))){
            throw new JwtException("Invalid Token Type");
        }
        return claims.getSubject();
    }

}
