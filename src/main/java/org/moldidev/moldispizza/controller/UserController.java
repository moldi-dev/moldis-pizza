package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        Page<UserDTO> result = userService.findAll(page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
               HTTPResponse
                       .builder()
                       .data(Map.of("usersDTOs", result))
                       .status(HttpStatus.OK)
                       .timestamp(LocalDateTime.now().toString())
                       .statusCode(HttpStatus.OK.value())
                       .build()
        );
    }

    @GetMapping("/id={id}")
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long userId) {
        UserDTO result = userService.findById(userId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/username={username}")
    public ResponseEntity<HTTPResponse> findByUsername(@PathVariable("username") String username) {
        UserDTO result = userService.findByUsername(username);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/email={email}")
    public ResponseEntity<HTTPResponse> findByEmail(@PathVariable("email") String email) {
        UserDTO result = userService.findByEmail(email);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/token={token}")
    public ResponseEntity<HTTPResponse> findByVerificationToken(@PathVariable("token") String token) {
        UserDTO result = userService.findByVerificationToken(token);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody User user) {
        UserDTO result = userService.save(user);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .message("Account successfully created. Follow the steps sent on the email in order to activate it")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyByVerificationToken(@RequestParam("email") String email, @RequestParam("token") String token) {
        return userService.verifyByVerificationToken(email, token);
    }

    @PatchMapping("/id={id}")
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long userId, @RequestBody User updatedUser) {
        UserDTO result = userService.updateById(userId, updatedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .message("User updated successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long userId) {
        userService.deleteById(userId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("User deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
