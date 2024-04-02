package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.dto.UserDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, BasketRepository basketRepository, OrderRepository orderRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

        if (!userList.isEmpty()) {
            return userList.stream().map(userDTOMapper).collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("there are no users");
    }

    public UserDTO getUserById(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            return userDTOMapper.apply(foundUser.get());
        }

        throw new ResourceNotFoundException("user not found by id: " + id);
    }

    public UserDTO getUserByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);

        if (foundUser.isPresent()) {
            return userDTOMapper.apply(foundUser.get());
        }

        throw new ResourceNotFoundException("user not found by username: " + username);
    }

    public UserDTO addUser(User user) throws ResourceAlreadyExistsException, InvalidArgumentException {
        Optional<User> alreadyExistentUser = userRepository.findUserByUsername(user.getUsername());
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        String usernameRegex = "^[a-zA-Z0-9_]{8,50}$";
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,200}$";

        if (alreadyExistentUser.isPresent()) {
            throw new ResourceAlreadyExistsException("user with username '" + user.getUsername() + "' already exists");
        }

        else if (!user.getUsername().matches(usernameRegex)) {
            throw new InvalidArgumentException("the username must consist of alphanumeric characters and underscores only, with a length between 8 and 50 characters");
        }

        else if (!user.getPassword().matches(passwordRegex)) {
            throw new InvalidArgumentException("the password must contain at least one lowercase letter, at least one uppercase letter, at least one digit, at least one special character and must be at least 8 characters long");
        }

        else if (!user.getEmail().matches(emailRegex)) {
            throw new InvalidArgumentException("the email must follow the standard 'example@example.example' format and must be at most 200 characters long");
        }

        String plainPassword = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(plainPassword);

        user.setPassword(encryptedPassword);

        Basket basket = new Basket();
        basket.setUser(user);
        basket.setPizzaList(new ArrayList<>());

        User savedUser = userRepository.save(user);
        basketRepository.save(basket);

        return userDTOMapper.apply(savedUser);
    }

    public UserDTO updateUserById(Long id, User newUser) throws ResourceNotFoundException, InvalidArgumentException {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();

            String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
            String usernameRegex = "^[a-zA-Z0-9_]{8,50}$";
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,200}$";

            if (!updatedUser.getUsername().matches(usernameRegex)) {
                throw new InvalidArgumentException("the username must consist of alphanumeric characters and underscores only, with a length between 8 and 50 characters");
            }

            else if (!updatedUser.getPassword().matches(passwordRegex)) {
                throw new InvalidArgumentException("the password must contain at least one lowercase letter, at least one uppercase letter, at least one digit, at least one special character and must be at least 8 characters long");
            }

            else if (!updatedUser.getEmail().matches(emailRegex)) {
                throw new InvalidArgumentException("the email must follow the standard 'example@example.example' format and must be at most 200 characters long");
            }

            updatedUser.setUsername(newUser.getUsername());
            updatedUser.setPassword(newUser.getPassword());
            updatedUser.setRole(newUser.getRole());

            User savedUser = userRepository.save(updatedUser);

            return userDTOMapper.apply(savedUser);
        }

        throw new ResourceNotFoundException("user not found by id: " + id);
    }

    public UserDTO updateUserByUsername(String username, User newUser) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);

        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();

            String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
            String usernameRegex = "^[a-zA-Z0-9_]{8,50}$";
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,200}$";

            if (!updatedUser.getUsername().matches(usernameRegex)) {
                throw new InvalidArgumentException("the username must consist of alphanumeric characters and underscores only, with a length between 8 and 50 characters");
            }

            else if (!updatedUser.getPassword().matches(passwordRegex)) {
                throw new InvalidArgumentException("the password must contain at least one lowercase letter, at least one uppercase letter, at least one digit, at least one special character and must be at least 8 characters long");
            }

            else if (!updatedUser.getEmail().matches(emailRegex)) {
                throw new InvalidArgumentException("the email must follow the standard 'example@example.example' format and must be at most 200 characters long");
            }

            updatedUser.setUsername(newUser.getUsername());
            updatedUser.setPassword(newUser.getPassword());
            updatedUser.setRole(newUser.getRole());

            User savedUser = userRepository.save(updatedUser);

            return userDTOMapper.apply(savedUser);
        }

        throw new ResourceNotFoundException("user not found by username: " + username);
    }

    @Transactional
    public void deleteUserById(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            orderRepository.deleteAllOrdersByUserId(id);
            basketRepository.deleteBasketByUserId(id);
            userRepository.deleteById(id);
        }

        else {
            throw new ResourceNotFoundException("user not found by id: " + id);
        }
    }

    @Transactional
    public void deleteUserByUsername(String username) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);

        if (foundUser.isPresent()) {
            orderRepository.deleteAllOrdersByUsername(username);
            basketRepository.deleteBasketByUsername(username);
            userRepository.deleteUserByUsername(username);
        }

        else {
            throw new ResourceNotFoundException("user not found by username: " + username);
        }
    }
}
