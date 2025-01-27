package authservice.service.impl;

import authservice.domain.AuthUserDetails;
import authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private final String SECRET =
            "6d3feed2420107c7a5e9f2dbd8b3e10ae8171486788518736cd40bf695052eeb5e6b4c4d4780eda88858c43437b8fb1d60d3d07cb918bbf912e9ffefa36108e7de1c7ebd14218dd304ad696fbb88378c1b332ce4e9c8e9da8fec272fcedc0f7b6f363201809a228d76a08d426a55808b15710ac7ee7969935be912dce43e5cf9ababf3d45d5606ca2129fec642ba5c7446a70fc4169b676a149ad69d103a3e4aa4c8d234c60cd7f05b6685bcbd679280aeb58bcfd0aa4e42e9a92322546c87854867497804979c6419a9c3a735ac371ee797b50c05af2f6ef5e2263ba488df09a43630ff3df2bbdfb2573c66ce641370a77be3e7e8f3f674d84f1e11048d05c6";


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String generateToken(Map<String, Object> claims, AuthUserDetails user) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("Abhi")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
