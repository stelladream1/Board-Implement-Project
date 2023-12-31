package com.its.member.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

import java.util.Date;

public class JWToken {


    public static  String getEmail(String token, String secretKey) throws SignatureException{

            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().get("email",String.class);

    }
    public static boolean isExpired(String token, String secretKey){
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
        }

        catch (SignatureException e){
            return false;
        }
    }

    public static String createJWT(String email, String secretKey){
        Claims claims = Jwts.claims();
        claims.put("email", email);

        long expirationMs = 360000000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
