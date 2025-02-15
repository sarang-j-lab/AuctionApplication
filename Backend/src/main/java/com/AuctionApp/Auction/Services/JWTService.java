package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import static jdk.jfr.internal.EventWriterKey.getKey;

@Service
public class JWTService {

    private  String secretKey = "";

    public JWTService(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            keyGen.init(512);
            SecretKey sk  = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("Internal server error", HttpStatus.SERVICE_UNAVAILABLE,"Something went wrong please try again after some hours.");
        }
    }

    public String extractMobileNo(String token) {
        //extract the username from jwt token
        return extractClaim(token,Claims::getSubject);
    }


    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
            final Claims claims = Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claimResolver.apply(claims);
    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String mobileNo = extractMobileNo(token);
        return (mobileNo.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public  boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String generateToken(String mobileNo) {

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(mobileNo)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .and()
                .signWith(SignatureAlgorithm.HS512,getKey())
                .compact();
    }

    private  SecretKey getKey() {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(byteKey);
    }
}
