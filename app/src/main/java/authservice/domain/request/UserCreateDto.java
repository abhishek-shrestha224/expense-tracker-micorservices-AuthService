package authservice.domain.request;

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