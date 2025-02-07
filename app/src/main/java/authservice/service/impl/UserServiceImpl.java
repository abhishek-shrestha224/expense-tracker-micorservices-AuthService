package authservice.service.impl;

import authservice.domain.request.UserCreateDto;
import authservice.domain.enity.UserEntity;
import authservice.repository.UserRepository;
import authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean checkUserExistsByUsername(String username) {
        final UserEntity user = userRepository.findByUsername(username).orElse(null);
        return null != user;
    }

    @Override
    public Boolean signupUser(UserCreateDto userCreateDto) {

        if (checkUserExistsByUsername(userCreateDto.username())) return false;

        UserEntity user = UserEntity.builder()
                .fName(userCreateDto.fName())
                .lName(userCreateDto.lName())
                .email(userCreateDto.email())
                .username(userCreateDto.username())
                .password(passwordEncoder.encode(userCreateDto.password()))
                .roles(new HashSet<>())
                .build();
        userRepository.save(user);
        return true;
    }
}
