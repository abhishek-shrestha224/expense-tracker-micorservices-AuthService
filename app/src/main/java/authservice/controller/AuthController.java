package authservice.controller;

import authservice.domain.AuthUserDetails;
import authservice.domain.enity.RefreshTokenEntity;
import authservice.domain.request.UserCreateDto;
import authservice.domain.response.JwtResponse;
import authservice.service.JwtService;
import authservice.service.RefreshTokenService;
import authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<JwtResponse> signUp(@RequestBody UserCreateDto createData) {
        try {
            Boolean isSignedUp = userService.signupUser(createData);
            if (Boolean.FALSE.equals(isSignedUp)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(createData.username());

            final AuthUserDetails userDetails = AuthUserDetails.builder()
                    .username(createData.username())
                    .password(createData.password())
                    .build();

            String jwtToken = jwtService.generateToken(Map.of("ref", false), userDetails);

            return ResponseEntity.status(HttpStatus.OK).body(
                    JwtResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken.getToken())
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
