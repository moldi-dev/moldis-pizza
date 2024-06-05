package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.UserDTO;
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
@RequestMapping("/auth")
public class AuthenticationController {
    private final JWTService jwtService;
    private final UserService userService;

    public AuthenticationController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User authenticatedUser = userService.authenticate(username, password);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        Long expiresIn = jwtService.getExpirationTime();

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(username, jwtToken, expiresIn);

        return ResponseEntity.ok(authenticationResponse);
    }
}
