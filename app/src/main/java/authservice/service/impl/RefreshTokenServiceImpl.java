package authservice.service.impl;

import authservice.domain.enity.RefreshTokenEntity;
import authservice.domain.enity.UserEntity;
import authservice.domain.exception.TokenExpiredException;
import authservice.repository.RefreshTokenRepository;
import authservice.repository.UserRepository;
import authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenServiceImpl(final RefreshTokenRepository refreshTokenRepository, final UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RefreshTokenEntity createRefreshToken(String username) {

        final UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (null == user) return null;

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(86400000))
                .user(user)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElse(null);
    }

    @Override
    public Boolean isTokenExpired(RefreshTokenEntity token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            return true;
        }

        return false;
    }

    @Override
    public Boolean deleteRefreshToken(String token) {
       final RefreshTokenEntity refreshToken= refreshTokenRepository.findByToken(token).orElse(null);
       if (null == refreshToken) return false;

       refreshTokenRepository.delete(refreshToken);
       return true;

    }
}
