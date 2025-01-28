package authservice.service;

import authservice.domain.enity.RefreshTokenEntity;
import authservice.domain.exception.TokenExpiredException;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(String username);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) throws TokenExpiredException;

    RefreshTokenEntity findByToken(String token);
}
