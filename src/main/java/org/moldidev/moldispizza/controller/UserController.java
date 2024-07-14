package org.moldidev.moldispizza.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.enumeration.Provider;
import org.moldidev.moldispizza.request.admin.UserCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.UserDetailsUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.*;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<HTTPResponse> findByUsername(@PathVariable("username") String username, Authentication connectedUser) {
        UserDTO result = userService.findByUsername(username, connectedUser);

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

    @GetMapping("/enabled/id={id}")
    public ResponseEntity<HTTPResponse> checkIfUserIsEnabled(@PathVariable("id") Long userId, Authentication connectedUser) {
        Boolean result = userService.checkIfUserIsEnabled(userId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("answer", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/provider/id={id}")
    public ResponseEntity<HTTPResponse> getUserProvider(@PathVariable("id") Long userId, Authentication connectedUser) {
        Provider provider = userService.findUserProvider(userId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message(provider.toString())
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody UserCreateAdminRequest request) {
        UserDTO result = userService.save(request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .message("User successfully created.")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/verify")
    public ResponseEntity<HTTPResponse> verifyByVerificationToken(@RequestBody UserActivateAccountRequest request) {
        UserDTO result = userService.verifyByVerificationToken(request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Your account has been successfully activated. You may now sign in")
                        .data(Map.of("userDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/complete-registration-oauth2-user/id={id}")
    public ResponseEntity<HTTPResponse> completeRegistrationForOAuth2User(@PathVariable("id") Long userId, @RequestBody CompleteRegistrationOAuth2UserRequest request, Authentication connectedUser) {
        Map<String, String> tokens = userService.completeRegistrationForOAuth2User(userId, request, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Your account has been successfully set up. Welcome " + request.username())
                        .data(tokens)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/admin/id={userId}")
    public ResponseEntity<HTTPResponse> checkIfUserIsAdmin(@PathVariable("userId") Long userId) {
        Boolean result = userService.checkIfUserIsAdmin(userId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("answer", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/resend-confirmation-email/email={email}")
    public ResponseEntity<HTTPResponse> resendConfirmationEmail(@PathVariable("email") String email) {
        userService.resendConfirmationEmail(email);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("The confirmation email has been sent again successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/send-reset-password-token")
    public ResponseEntity<HTTPResponse> sendResetPasswordToken(@RequestBody UserResetPasswordEmailRequest request) {
        userService.sendResetPasswordEmail(request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("The password reset code has been sent successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HTTPResponse> resetPasswordThroughToken(@RequestBody UserResetPasswordCodeRequest request) {
        UserDTO result = userService.resetPasswordThroughToken(request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("The account's password has been reset successfully. You may now sign in")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("userDTO", result))
                        .build()
        );
    }

    @PostMapping("/change-password/id={id}")
    public ResponseEntity<HTTPResponse> changePasswordById(@PathVariable("id") Long userId, @RequestBody UserChangePasswordRequest request, Authentication connectedUser) {
        UserDTO result = userService.changePasswordById(userId, request, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Your password has been changed successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("userDTO", result))
                        .build()
        );
    }

    @PatchMapping("/id={id}")
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long userId, @RequestBody UserDetailsUpdateRequest request, Authentication connectedUser) {
        UserDTO result = userService.updateById(userId, request, connectedUser);

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

    @PatchMapping("/admin/id={id}")
    public ResponseEntity<HTTPResponse> updateByIdAdminRequest(@PathVariable("id") Long userId, @RequestBody UserDetailsUpdateAdminRequest request) {
        UserDTO result = userService.updateById(userId, request);

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

    @PatchMapping("/set-image/id={user_id}/image-id={image_id}")
    public ResponseEntity<HTTPResponse> setUserImage(@PathVariable("user_id") Long userId, @PathVariable("image_id") Long imageId, Authentication connectedUser) {
        UserDTO result = userService.setUserImage(userId, imageId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .message("User's image set successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/remove-image/id={user_id}")
    public ResponseEntity<HTTPResponse> removeUserImage(@PathVariable("user_id") Long userId, Authentication connectedUser) {
        UserDTO result = userService.removeUserImage(userId, connectedUser);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("userDTO", result))
                        .message("User's image removed successfully")
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
