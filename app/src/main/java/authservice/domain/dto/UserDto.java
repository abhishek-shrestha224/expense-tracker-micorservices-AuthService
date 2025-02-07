package authservice.domain.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String fName,
        String lName,
        String username,
        String password
) {
}
