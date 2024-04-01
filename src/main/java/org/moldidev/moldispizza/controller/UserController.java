package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
       return ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody User newUser) {
       return ResponseEntity.ok(userService.updateUserById(id, newUser));
    }

    @PostMapping("/updateUserByUsername/{username}")
    public ResponseEntity<UserDTO> updateUserByUsername(@PathVariable String username, @RequestBody User newUser) {
        return ResponseEntity.ok(userService.updateUserByUsername(username, newUser));
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserByUsername/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
