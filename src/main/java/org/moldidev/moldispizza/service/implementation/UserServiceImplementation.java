package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.*;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.exception.ObjectNotValidException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.UserDTOMapper;
import org.moldidev.moldispizza.repository.*;
import org.moldidev.moldispizza.service.*;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final BasketRepository basketRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ObjectValidator<User> objectValidator;
    private final SecurityService securityService;
    private final JWTService jwtService;

    @Override
    public UserDTO save(User user) {
        objectValidator.validate(user);
        HashSet<String> validationErrors = new HashSet<>();

        Optional<User> foundUser = userRepository.findByUsernameIgnoreCase(user.getUsername());

        if (foundUser.isPresent()) {
            throw new ResourceAlreadyExistsException("This username is already taken");
        }

        Optional<User> foundUserByEmail = userRepository.findByEmailIgnoreCase(user.getEmail());

        if (foundUserByEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("This email address is already taken");
        }

        if (user.getPassword().length() < 8) {
            validationErrors.add("The password must be at least 8 characters long");
            throw new ObjectNotValidException(validationErrors);
        }

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

        return userDTOMapper.apply(savedUser);
    }

    @Override
    public Map<String, String> signIn(Map<String, Object> credentials) {
        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");
        boolean rememberMe = Boolean.parseBoolean((String) credentials.get("rememberMe"));

        User authenticatedUser = authenticate(username, password);

        String accessToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60); // 60 minutes = one hour
        String refreshToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60 * 24); // 24 hours = one day
        String rememberMeToken = null;

        if (rememberMe) {
            rememberMeToken = jwtService.generateToken(authenticatedUser, 1000L * 60 * 60 * 24 * 30); // 30 days = one month
        }

        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        if (rememberMeToken != null) {
            tokens.put("rememberMeToken", rememberMeToken);
        }

        return tokens;
    }

    @Override
    public User authenticate(String username, String password) {
        User foundUser = userRepository.findByUsernameIgnoreCase(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials provided"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return foundUser;
    }

    @Override
    public UserDTO findById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByUsername(String username, Authentication connectedUser) {
        User foundUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided username doesn't exist"));

        securityService.validateAuthenticatedUser(connectedUser, foundUser.getUserId());

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User foundUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exis"));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public UserDTO findByVerificationToken(String verificationToken) {
        User foundUser = userRepository.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided verification code doesn't exist"));

        return userDTOMapper.apply(foundUser);
    }

    @Override
    public Page<UserDTO> findAll(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users exist");
        }

        return users.map(userDTOMapper);
    }

    @Override
    public ResponseEntity<String> verifyByVerificationToken(String email, String verificationToken) {
        User foundUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exist"));

        if (foundUser.getIsEnabled()) {
            return new ResponseEntity<>("Your account is already verified", HttpStatus.CONFLICT);
        }

        if (foundUser.getVerificationToken().equals(verificationToken)) {
            foundUser.setIsEnabled(true);
            userRepository.save(foundUser);
            return new ResponseEntity<>("Your account has been successfully verified. You may now sign in", HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>("The verification token is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void resendConfirmationEmail(String email) {
        User foundUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exist"));

        if (foundUser.getIsEnabled()) {
            throw new ResourceAlreadyExistsException("This user is already verified");
        }

        emailService.sendCompleteRegistrationEmail(foundUser.getEmail(), foundUser.getVerificationToken());
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        User foundUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exist"));

        foundUser.setResetPasswordToken(generateResetPasswordToken(foundUser));
        userRepository.save(foundUser);

        emailService.sendResetPasswordEmail(foundUser.getEmail(), foundUser.getResetPasswordToken());
    }

    @Override
    public UserDTO resetPasswordThroughToken(String token, String newPassword) {
        User foundUser = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided token doesn't exist"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        foundUser.setPassword(encoder.encode(newPassword));

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO changePasswordById(Long userId, String currentPassword, String newPassword, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(currentPassword, foundUser.getPassword())) {
            throw new BadCredentialsException("Invalid current password provided");
        }

        foundUser.setPassword(encoder.encode(newPassword));
        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO setUserImage(Long userId, Long imageId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        foundUser.setImage(foundImage);

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO removeUserImage(Long userId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Image userImage = foundUser.getImage();

        if (userImage != null) {
            foundUser.setImage(null);
            imageService.delete(userImage);
            return userDTOMapper.apply(userRepository.save(foundUser));
        }

        throw new ResourceNotFoundException("This user has no image");
    }

    @Override
    public UserDTO updateById(Long userId, User updatedUser, Authentication connectedUser) {
        HashSet<String> validationErrors = new HashSet<>();

        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        objectValidator.validate(updatedUser);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String givenPassword = updatedUser.getPassword();
        String foundPassword = foundUser.getPassword();

        if (!encoder.matches(givenPassword, foundPassword)) {
            validationErrors.add("Invalid password provided");
            throw new ObjectNotValidException(validationErrors);
        }

        if (updatedUser.getImage() != null) {
            foundUser.setImage(updatedUser.getImage());
        }

        foundUser.setFirstName(updatedUser.getFirstName());
        foundUser.setLastName(updatedUser.getLastName());
        foundUser.setAddress(updatedUser.getAddress());

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public void deleteById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Page<Order> foundUserOrders = orderRepository.findAllByUserUserId(userId, null);
        Page<Review> foundUserReviews = reviewRepository.findAllByUserUserId(userId, null);
        Optional<Basket> foundUserBasket = basketRepository.findByUserUserId(userId);
        Optional<Image> foundUserImage = imageRepository.findByUserId(userId);

        foundUserBasket.ifPresent(basketRepository::delete);
        foundUserImage.ifPresent(imageService::delete);

        if (!foundUserReviews.isEmpty()) {
            reviewRepository.deleteAll(foundUserReviews);
        }

        if (!foundUserOrders.isEmpty()) {
            orderRepository.deleteAll(foundUserOrders);
        }

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

    private String generateResetPasswordToken(User user) {
        UUID uuid = UUID.randomUUID();
        String username = user.getUsername();
        String resetPasswordCode = uuid + username;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(resetPasswordCode.getBytes());

            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);


            return base64Encoded.substring(0, 22);
        }

        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
