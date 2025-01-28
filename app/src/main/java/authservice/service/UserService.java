package authservice.service;

import authservice.domain.request.UserCreateDto;

public interface UserService {

    Boolean checkUserExistsByUsername(String username);

    Boolean signupUser(UserCreateDto userCreateDto);
}
