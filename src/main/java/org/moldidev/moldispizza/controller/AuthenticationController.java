package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.JWTService;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JWTService jwtService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<HTTPResponse> signUp(@RequestBody User user) {
        UserDTO createdUser = userService.save(user);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message("Account successfully created. Follow the steps sent on the email in order to activate it.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(Map.of("userDTO", createdUser))
                        .build()
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<HTTPResponse> signIn(@RequestBody Map<String, Object> credentials) {
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

        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        if (rememberMeToken != null) {
            tokens.put("rememberMeToken", rememberMeToken);
        }

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message("Sign in successful. Welcome " + username)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(tokens)
                        .build()
        );
    }
}
