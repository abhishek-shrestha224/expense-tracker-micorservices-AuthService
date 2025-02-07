package authservice.service;

import authservice.domain.AuthUserDetails;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    public String generateToken(Map<String, Object> claims, AuthUserDetails user);

    public String generateToken(String username);

    public String extractUsername(String token);

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver);

    public Claims extractAllClaims(String token);

    public Date extractExpiration(String token);

    public Boolean isTokenExpired(String token);

    public Boolean isTokenValid(String token, UserDetails userDetails);

   }
