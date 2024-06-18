package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

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
        Map<String, String> tokens = userService.signIn(credentials);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message("Sign in successful. Welcome " + credentials.get("username"))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(tokens)
                        .build()
        );
    }
}
