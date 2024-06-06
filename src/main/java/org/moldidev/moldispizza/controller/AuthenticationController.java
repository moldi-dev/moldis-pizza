package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.service.JWTService;
import org.moldidev.moldispizza.service.UserService;
import org.moldidev.moldispizza.utility.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final JWTService jwtService;
    private final UserService userService;

    public AuthenticationController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody Map<String, Object> credentials) {
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");
        boolean rememberMe = Boolean.parseBoolean((String) credentials.get("rememberMe"));

        User authenticatedUser = userService.authenticate(username, password);

        String accessToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60); // 60 minutes = one hour
        String refreshToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60 * 24); // 24 hours = one day
        String rememberMeToken = null;

        if (rememberMe) {
            rememberMeToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60 * 24 * 30); // 30 days = one month
        }

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken, rememberMeToken);

        return ResponseEntity.ok(authenticationResponse);
    }
}
