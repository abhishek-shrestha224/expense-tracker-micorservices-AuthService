package authservice.controller;

import authservice.domain.request.UserCreateDto;
import authservice.service.JwtService;
import authservice.service.RefreshTokenService;
import authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    @Autowired
    public AuthController(final JwtService jwtService, final RefreshTokenService refreshTokenService, final UserService userService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String SignUp(@RequestBody UserCreateDto createData) {
        return null;
    }
}
