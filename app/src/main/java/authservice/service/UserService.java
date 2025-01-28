package authservice.service;

public interface UserService {

    Boolean checkUserExistsByUsername(String username);

    Boolean signupUser(String email);
}
