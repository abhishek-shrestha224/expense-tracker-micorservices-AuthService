package authservice.domain.dto;

import authservice.domain.enity.UserEntity;
import lombok.Builder;

import java.time.Instant;

@Builder
public record RefreshTokenDto(
        String token,
        Instant expiresAt,
        UserEntity user
) {
}
