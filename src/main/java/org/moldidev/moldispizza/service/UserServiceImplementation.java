package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.UserDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final BasketRepository basketRepository;
    private final AuthenticationManager authenticationManager;

    public UserServiceImplementation(UserRepository userRepository, UserDTOMapper userDTOMapper, BasketRepository basketRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.basketRepository = basketRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDTO save(User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User " + user.getUsername() + " already exists");
        }

        if (checkIfUserIsValid(user)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            user.setPassword(encoder.encode(user.getPassword()));
            user.setRole(Role.CUSTOMER);
            user.setIsLocked(false);

            User savedUser = userRepository.save(user);

            Basket basket = new Basket();
            basket.setUser(user);
            basket.setTotalPrice(0.0);
            basket.setPizzas(new ArrayList<>());

            basketRepository.save(basket);

            return userDTOMapper.apply(savedUser);
        }

        return null;
    }

    @Override
    public User authenticate(String username, String password) {
        User foundUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

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
    public UserDTO updateById(Long userId, User updatedUser) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by user id " + userId));

        if (checkIfUserIsValid(updatedUser)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            foundUser.setPassword(encoder.encode(updatedUser.getPassword()));
            foundUser.setImage(updatedUser.getImage());
            foundUser.setFirstName(updatedUser.getFirstName());
            foundUser.setLastName(updatedUser.getLastName());
            foundUser.setEmail(updatedUser.getEmail());
            foundUser.setAddress(updatedUser.getAddress());

            if (updatedUser.getRole() != null) {
                foundUser.setRole(updatedUser.getRole());
            }

            else {
                foundUser.setRole(Role.CUSTOMER);
            }


            if (updatedUser.getIsLocked() != null) {
                foundUser.setIsLocked(updatedUser.getIsLocked());
            }

            else {
                foundUser.setIsLocked(false);
            }

            return userDTOMapper.apply(userRepository.save(foundUser));
        }

        return null;
    }

    @Override
    public UserDTO updateByUsername(String username, User updatedUser) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by username " + username));

        if (checkIfUserIsValid(updatedUser)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            foundUser.setPassword(encoder.encode(updatedUser.getPassword()));
            foundUser.setImage(updatedUser.getImage());
            foundUser.setFirstName(updatedUser.getFirstName());
            foundUser.setLastName(updatedUser.getLastName());
            foundUser.setEmail(updatedUser.getEmail());
            foundUser.setAddress(updatedUser.getAddress());

            if (updatedUser.getRole() != null) {
                foundUser.setRole(updatedUser.getRole());
            }

            else {
                foundUser.setRole(Role.CUSTOMER);
            }


            if (updatedUser.getIsLocked() != null) {
                foundUser.setIsLocked(updatedUser.getIsLocked());
            }

            else {
                foundUser.setIsLocked(false);
            }

            return userDTOMapper.apply(userRepository.save(foundUser));
        }

        return null;
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

    private boolean checkIfUserIsValid(User user) {
        if (user.getUsername() == null) {
            throw new InvalidArgumentException("The username can't be null");
        }

        else if (user.getUsername().isEmpty()) {
            throw new InvalidArgumentException("The username can't be empty");
        }

        else if (user.getUsername().isBlank()) {
            throw new InvalidArgumentException("The username can't be blank");
        }

        else if (user.getUsername().length() < 10 || user.getUsername().length() > 100) {
            throw new InvalidArgumentException("The username must contain at least 10 characters and at most 100 characters");
        }

        else if (user.getPassword() == null) {
            throw new InvalidArgumentException("The password can't be null");
        }

        else if (user.getPassword().isEmpty()) {
            throw new InvalidArgumentException("The password can't be empty");
        }

        else if (user.getPassword().isBlank()) {
            throw new InvalidArgumentException("The password can't be blank");
        }

        else if (user.getFirstName() == null) {
            throw new InvalidArgumentException("The first name can't be null");
        }

        else if (user.getFirstName().isEmpty()) {
            throw new InvalidArgumentException("The first name can't be empty");
        }

        else if (user.getFirstName().isBlank()) {
            throw new InvalidArgumentException("The first name can't be blank");
        }

        else if (user.getFirstName().length() > 50) {
            throw new InvalidArgumentException("The first name can contain at most than 50 characters");
        }

        else if (user.getLastName() == null) {
            throw new InvalidArgumentException("The last name can't be null");
        }

        else if (user.getLastName().isEmpty()) {
            throw new InvalidArgumentException("The last name can't be empty");
        }

        else if (user.getLastName().isBlank()) {
            throw new InvalidArgumentException("The last name can't be blank");
        }

        else if (user.getLastName().length() > 50) {
            throw new InvalidArgumentException("The last name can contain at most 50 characters");
        }

        else if (user.getEmail() == null) {
            throw new InvalidArgumentException("The email can't be null");
        }

        else if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidArgumentException("The email must follow the pattern 'email@domain.com'");
        }

        else if (user.getAddress() == null) {
            throw new InvalidArgumentException("The address can't be null");
        }

        else if (user.getAddress().isEmpty()) {
            throw new InvalidArgumentException("The address can't be empty");
        }

        else if (user.getAddress().isBlank()) {
            throw new InvalidArgumentException("The address can't be blank");
        }

        return true;
    }
}
