package authservice.domain.dto;

import lombok.Builder;

@Builder
public record UserCreateDto(
        String fName,
        String lName,
        String username,
        String email,
        String password
) {

}
