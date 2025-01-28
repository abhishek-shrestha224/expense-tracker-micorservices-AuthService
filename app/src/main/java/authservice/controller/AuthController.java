package authservice.controller;

import authservice.domain.enity.RefreshTokenEntity;
import authservice.domain.enity.UserEntity;
import authservice.domain.request.LoginRequestDto;
import authservice.domain.request.TokenRefreshDto;
import authservice.domain.request.UserCreateDto;
import authservice.domain.response.JwtResponse;
import authservice.service.JwtService;
import authservice.service.RefreshTokenService;
import authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(final JwtService jwtService, final RefreshTokenService refreshTokenService, final UserService userService, final AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.authManager = authManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signUp(@RequestBody UserCreateDto createData) {
        try {
            Boolean isSignedUp = userService.signupUser(createData);
            if (Boolean.FALSE.equals(isSignedUp)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(createData.username());

            String jwtToken = jwtService.generateToken(createData.username());

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

    @PostMapping("/login")
     public ResponseEntity<String> authenticateAndGetToken(@RequestBody LoginRequestDto credentials) {

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password()));

            if (!auth.isAuthenticated()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("auth failed");

            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(credentials.username());
            return ResponseEntity.status(HttpStatus.OK).body(JwtResponse.builder()
                    .accessToken(jwtService.generateToken(credentials.username()))
                    .refreshToken(refreshToken.getToken())
                    .build().toString());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }


    }
@PostMapping("/logout")
public ResponseEntity<String> logout(@RequestBody TokenRefreshDto tokenRefreshDto) {
        final Boolean isLoggedOut = refreshTokenService.deleteRefreshToken(tokenRefreshDto.token());

        if(Boolean.FALSE.equals(isLoggedOut)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed");

        return ResponseEntity.status(HttpStatus.OK).body("Successfully logged out");
}

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody TokenRefreshDto tokenRefreshDto) {
        RefreshTokenEntity refreshToken = refreshTokenService.findByToken(tokenRefreshDto.token());
        if (refreshToken == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        if (Boolean.TRUE.equals(refreshTokenService.isTokenExpired(refreshToken))) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        final UserEntity user = refreshToken.getUser();

        final String accessToken = jwtService.generateToken(user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(
                JwtResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken.getToken())
                        .build()
        );
    }


}
