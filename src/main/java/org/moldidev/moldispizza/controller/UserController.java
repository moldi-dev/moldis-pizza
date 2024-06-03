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

    @PostMapping("/save")
    public ResponseEntity<UserDTO> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/update/id={user_id}")
    public ResponseEntity<UserDTO> updateById(@PathVariable("user_id") Long userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateById(userId, user));
    }

    @PostMapping("/update/username={username}")
    public ResponseEntity<UserDTO> updateByUsername(@PathVariable("username") String username, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateByUsername(username, user));
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
