package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.exception.InvalidInputException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.UserDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final BasketRepository basketRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public UserServiceImplementation(UserRepository userRepository, UserDTOMapper userDTOMapper, BasketRepository basketRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.basketRepository = basketRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<String> save(User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User " + user.getUsername() + " already exists");
        }

        Optional<User> foundUserByEmail = userRepository.findByEmail(user.getEmail());

        if (foundUserByEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("Email " + user.getEmail() + " is already taken");
        }

        checkIfUserIsValid(user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String verificationToken = generateVerificationToken(user);

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setIsLocked(false);
        user.setIsEnabled(false);
        user.setVerificationToken(verificationToken);

        User savedUser = userRepository.save(user);

        Basket basket = new Basket();
        basket.setUser(savedUser);
        basket.setTotalPrice(0.0);
        basket.setPizzas(new ArrayList<>());

        basketRepository.save(basket);

        emailService.sendCompleteRegistrationEmail(savedUser.getEmail(), verificationToken);

        return new ResponseEntity<>("Account successfully created. Follow the steps sent to the email '" + savedUser.getEmail() + "' in order to activate your account.", HttpStatus.OK);
    }

    @Override
    public User authenticate(String username, String password) {
        User foundUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return foundUser;
    }

    @Override
    public UserDTO findById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by email " + email));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByVerificationToken(String verificationToken) {
        User foundUser = userRepository.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by verification code " + verificationToken));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        return users
                .stream()
                .map(userDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> verifyByVerificationToken(String email, String verificationToken) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by email " + email));

        if (foundUser.getIsEnabled()) {
            return new ResponseEntity<>("Your account is already verified", HttpStatus.CONFLICT);
        }

        if (foundUser.getVerificationToken().equals(verificationToken)) {
            foundUser.setIsEnabled(true);
            userRepository.save(foundUser);
            return new ResponseEntity<>("Your account has been successfully verified. You can now log in", HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("The verification code is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDTO updatePassword(Long userId, String oldPassword, String newPassword) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(oldPassword, foundUser.getPassword())) {
            throw new InvalidInputException("Old password is incorrect");
        }

        foundUser.setPassword(encoder.encode(newPassword));

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO updateById(Long userId, User updatedUser) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by user id " + userId));

        checkIfUserIsValid(updatedUser);

        foundUser.setEmail(updatedUser.getEmail());
        foundUser.setImage(updatedUser.getImage());
        foundUser.setFirstName(updatedUser.getFirstName());
        foundUser.setLastName(updatedUser.getLastName());
        foundUser.setAddress(updatedUser.getAddress());

        if (updatedUser.getRole() != null) {
            foundUser.setRole(updatedUser.getRole());
        }

        if (updatedUser.getIsLocked() != null) {
            foundUser.setIsLocked(updatedUser.getIsLocked());
        }

        if (updatedUser.getIsEnabled() != null) {
            foundUser.setIsEnabled(updatedUser.getIsEnabled());
        }

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO updateByUsername(String username, User updatedUser) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

        checkIfUserIsValid(updatedUser);

        foundUser.setEmail(updatedUser.getEmail());
        foundUser.setImage(updatedUser.getImage());
        foundUser.setFirstName(updatedUser.getFirstName());
        foundUser.setLastName(updatedUser.getLastName());
        foundUser.setAddress(updatedUser.getAddress());

        if (updatedUser.getRole() != null) {
            foundUser.setRole(updatedUser.getRole());
        }

        if (updatedUser.getIsLocked() != null) {
            foundUser.setIsLocked(updatedUser.getIsLocked());
        }

        if (updatedUser.getIsEnabled() != null) {
            foundUser.setIsEnabled(updatedUser.getIsEnabled());
        }

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public void deleteById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by user id " + userId));

        Optional<Basket> foundUserBasket = basketRepository.findByUserUserId(userId);

        foundUserBasket.ifPresent(basketRepository::delete);

        userRepository.delete(foundUser);
    }

    @Override
    public void deleteByUsername(String username) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

        Optional<Basket> foundUserBasket = basketRepository.findByUserUserId(foundUser.getUserId());

        foundUserBasket.ifPresent(basketRepository::delete);

        userRepository.delete(foundUser);
    }

    private String generateVerificationToken(User user) {
        UUID uuid = UUID.randomUUID();
        String username = user.getUsername();
        String verificationCode = uuid + username;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(verificationCode.getBytes());

            StringBuilder hashHex = new StringBuilder();

            for (byte b : hashBytes) {
                hashHex.append(String.format("%02x", b));
            }

            return hashHex.toString();
        }

        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private void checkIfUserIsValid(User user) {
        if (user.getUsername() == null) {
            throw new InvalidInputException("The username can't be null");
        }

        else if (user.getUsername().isEmpty()) {
            throw new InvalidInputException("The username can't be empty");
        }

        else if (user.getUsername().isBlank()) {
            throw new InvalidInputException("The username can't be blank");
        }

        else if (user.getUsername().length() < 10 || user.getUsername().length() > 100) {
            throw new InvalidInputException("The username must contain at least 10 characters and at most 100 characters");
        }

        else if (user.getPassword() == null) {
            throw new InvalidInputException("The password can't be null");
        }

        else if (user.getPassword().isEmpty()) {
            throw new InvalidInputException("The password can't be empty");
        }

        else if (user.getPassword().isBlank()) {
            throw new InvalidInputException("The password can't be blank");
        }

        else if (user.getFirstName() == null) {
            throw new InvalidInputException("The first name can't be null");
        }

        else if (user.getFirstName().isEmpty()) {
            throw new InvalidInputException("The first name can't be empty");
        }

        else if (user.getFirstName().isBlank()) {
            throw new InvalidInputException("The first name can't be blank");
        }

        else if (user.getFirstName().length() > 50) {
            throw new InvalidInputException("The first name can contain at most than 50 characters");
        }

        else if (user.getLastName() == null) {
            throw new InvalidInputException("The last name can't be null");
        }

        else if (user.getLastName().isEmpty()) {
            throw new InvalidInputException("The last name can't be empty");
        }

        else if (user.getLastName().isBlank()) {
            throw new InvalidInputException("The last name can't be blank");
        }

        else if (user.getLastName().length() > 50) {
            throw new InvalidInputException("The last name can contain at most 50 characters");
        }

        else if (user.getEmail() == null) {
            throw new InvalidInputException("The email can't be null");
        }

        else if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputException("The email must follow the pattern 'email@domain.com'");
        }

        else if (user.getAddress() == null) {
            throw new InvalidInputException("The address can't be null");
        }

        else if (user.getAddress().isEmpty()) {
            throw new InvalidInputException("The address can't be empty");
        }

        else if (user.getAddress().isBlank()) {
            throw new InvalidInputException("The address can't be blank");
        }
    }
}
