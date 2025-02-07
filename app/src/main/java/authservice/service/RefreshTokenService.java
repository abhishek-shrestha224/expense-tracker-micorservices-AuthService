package authservice.service;

import authservice.domain.enity.RefreshTokenEntity;
import authservice.domain.exception.TokenExpiredException;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(String username);

    Boolean isTokenExpired(RefreshTokenEntity token) ;

    RefreshTokenEntity findByToken(String token);

    Boolean deleteRefreshToken(String token);
}
