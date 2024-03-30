package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User addUser(User user) {
        String plainPassword = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public Optional<User> updateUserById(Long id, User newUser) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();

            updatedUser.setUsername(newUser.getUsername());
            updatedUser.setPassword(newUser.getPassword());
            updatedUser.setRole(newUser.getRole());

            return Optional.of(userRepository.save(updatedUser));
        }

        return Optional.empty();
    }

    public Optional<User> updateUserByUsername(String username, User newUser) {
        Optional<User> foundUser = userRepository.findUserByUsername(username);

        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();

            updatedUser.setUsername(newUser.getUsername());
            updatedUser.setPassword(newUser.getPassword());
            updatedUser.setRole(newUser.getRole());

            return Optional.of(userRepository.save(updatedUser));
        }

        return Optional.empty();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }
}
