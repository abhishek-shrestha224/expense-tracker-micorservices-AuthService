package authservice.domain.response;

import lombok.Builder;

@Builder
public record JwtResponse(
        String refreshToken,
        String accessToken
) {
}
