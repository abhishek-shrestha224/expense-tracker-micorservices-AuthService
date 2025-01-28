package authservice.service.impl;

import authservice.domain.enity.UserEntity;
import authservice.repository.UserRepository;
import authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean checkUserExistsByUsername(String username) {
        final UserEntity user = userRepository.findByUsername(username).orElse(null);
        return null != user;
    }
}
