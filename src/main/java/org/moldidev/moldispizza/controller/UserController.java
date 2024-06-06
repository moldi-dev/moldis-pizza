package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/find/id={user_id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/find/username={username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/find/email={email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/find/verification-token={verification_token}")
    public ResponseEntity<UserDTO> findByVerificationToken(@PathVariable("verification_token") String verificationToken) {
        return ResponseEntity.ok(userService.findByVerificationToken(verificationToken));
    }

    @GetMapping("/verify/email={email}/verification-token={verification_token}")
    public ResponseEntity<String> verifyAccountByToken(@PathVariable("email") String email, @PathVariable("verification_token") String verificationToken) {
        return userService.verifyByVerificationToken(email, verificationToken);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/update/id={user_id}")
    public ResponseEntity<UserDTO> updateById(@PathVariable("user_id") Long userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateById(userId, user));
    }

    @PostMapping("/update/username={username}")
    public ResponseEntity<UserDTO> updateByUsername(@PathVariable("username") String username, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateByUsername(username, user));
    }

    @PostMapping("/update-password/user-id={user_id}/old-password={old_password}/new-password={new_password}")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable("user_id") Long userId, @PathVariable("old_password") String oldPassword, @PathVariable("new_password") String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(userId, oldPassword, newPassword));
    }

    @DeleteMapping("/delete/id={user_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("user_id") Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/username={username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable("username") String username) {
        userService.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
