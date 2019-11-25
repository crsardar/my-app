package com.crsardar.handson.java.springboot.jwt.controller;

import io.jsonwebtoken.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author Chittaranjan Sardar
 */
@RestController
public class JWTController {

    private final String MY_SECRET = "Chittaranjan";

    @PostMapping("/validate-token-java")
    public User validateToken(@RequestBody final User user){

        System.out.println("Received at validateUser : " + user);

        user.setMessage(validateToken(user.getToken()));

        return user;
    }

    @PostMapping("/generate-token-java")
    public User generateToken(@RequestBody final User user){

        System.out.println("Received at generateToken : " + user);

        user.setToken(generateToken(user.getUserId()));

        user.setMessage("You have valid token, enjoy!");

        return user;
    }

    private String validateToken(final String token){

        String result;
        try {

            Claims claims = Jwts.parser().setSigningKey(MY_SECRET).parseClaimsJws(token).getBody();

            result = "'" + claims.get("user") + "' has a valid token";

        }catch (Exception e){

            System.out.println("Failed in validateToken: ");
            e.printStackTrace();

            result = "This is NOT a valid token";
        }

        System.out.println("validateToken : " + result);

        return result;
    }


    private String generateToken(final String user){

        Instant now = Instant.now();
        Instant after = now.plus(Duration.ofMinutes(1));
        Date date = Date.from(after);

        Claims claims = Jwts.claims();
        claims.put("user", user);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setClaims(claims);
        jwtBuilder.signWith(SignatureAlgorithm.HS256, MY_SECRET);
        jwtBuilder.setExpiration(date);

        return jwtBuilder.compact();
    }

    public static class User{

        private String userId;
        private String token;
        private String message;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", token='" + token + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
