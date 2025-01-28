package authservice.service;

import authservice.domain.dto.UserCreateDto;

public interface UserService {

    Boolean checkUserExistsByUsername(String username);

    Boolean signupUser(UserCreateDto userCreateDto);
}
