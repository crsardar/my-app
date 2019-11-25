package com.crsardar.handson.java.springboot.jwt.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTJavaWithPublicPrivateKey
{

    public static void main(String[] args)
    {
        System.out.println("generating keys");
        Map<String, Object> rsaKeys = null;

        try
        {
            rsaKeys = getRSAKeys();
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
        PublicKey publicKey = (PublicKey) rsaKeys.get("public");
        PrivateKey privateKey = (PrivateKey) rsaKeys.get("private");

        System.out.println("generated keys");

        String token = generateToken(privateKey);
        System.out.println("Generated Token:\n" + token);

        verifyToken(token, publicKey);
    }

    public static String generateToken(PrivateKey privateKey)
    {
        String token = null;
        try
        {
            Instant now = Instant.now();
            Instant after = now.plus(Duration.ofMinutes(1));
            Date date = Date.from(after);

            Claims claims = Jwts.claims();
            claims.put("user", "CRSARDAR");
            claims.put("issuer", "Chittaranjan Sardar");
            claims.put("subject", "JWT Cross Platforms");
            claims.put("audience", "https://github.com/crsardar");
            claims.put("audience", "https://github.com/crsardar");
            claims.put("created", new Date());

            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setClaims(claims);
            jwtBuilder.setExpiration(date);
            jwtBuilder.signWith(SignatureAlgorithm.RS512, privateKey);

            token = jwtBuilder.compact();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return token;
    }

    // verify and get claims using public key

    private static Claims verifyToken(String token, PublicKey publicKey)
    {
        Claims claims;
        try
        {
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(publicKey);
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            claims = claimsJws.getBody();

            System.out.println("verifyToken : issuer = " + claims.get("issuer"));
        }
        catch (Exception e)
        {

            claims = null;
        }
        return claims;
    }

    private static Map<String, Object> getRSAKeys() throws Exception
    {

        Map<String, Object> keys = new HashMap();
        PrivateKey privateKey = getPrivateKey();
        PublicKey publicKey = getPublicKey();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

    private static PrivateKey getPrivateKey() throws Exception
    {
        ClassLoader classLoader = JWTJavaWithPublicPrivateKey.class.getClassLoader();
        URL resource = classLoader.getResource("my_key");
        File file = new File(resource.getFile());
        byte[] keyBytes = Files.readAllBytes(Paths.get(file.toURI()));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private static PublicKey getPublicKey() throws Exception
    {
        ClassLoader classLoader = JWTJavaWithPublicPrivateKey.class.getClassLoader();
        URL resource = classLoader.getResource("my_key.pub");
        File file = new File(resource.getFile());
        byte[] keyBytes = Files.readAllBytes(Paths.get(file.toURI()));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}