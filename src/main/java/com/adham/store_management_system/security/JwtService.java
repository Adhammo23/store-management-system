package com.adham.store_management_system.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;
    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date()) // وقت إنشاء الـ Token — دلوقتي
                .expiration(new Date(System.currentTimeMillis()+expiration))  // وقت الانتهاء
                .signWith(getSignKey()) // وقّع الـ Token بالـ SECRET_KEY — ده اللي بيعمل الـ Signature بياخد Bytes****
                .compact();
    }

    public String extractUserName(String token){
        return Jwts.parser()
                .verifyWith(getSignKey()) // اول حاجه اتاكد ان ال secret key صح
                .build()
                .parseSignedClaims(token)//  افتح الـ Token واستخلص محتواه — لو الـ Signature غلط هيـ throw Exception هنا
                // جيب الـ Payload وبعدين جيب الـ "sub" — اللي هو الـ username
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
