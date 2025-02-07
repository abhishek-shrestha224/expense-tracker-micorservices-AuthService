package authservice.domain.request;

public record LoginRequestDto(
        String username,
        String password
) {
}
