package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.*;
import org.moldidev.moldispizza.enumeration.Provider;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.exception.ObjectNotValidException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.UserDTOMapper;
import org.moldidev.moldispizza.repository.*;
import org.moldidev.moldispizza.request.admin.UserCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.UserDetailsUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.*;
import org.moldidev.moldispizza.service.*;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final SecurityService securityService;
    private final JWTService jwtService;

    private final ObjectValidator<UserSignInRequest> userSignInRequestValidator;
    private final ObjectValidator<UserSignUpRequest> userSignUpRequestValidator;
    private final ObjectValidator<UserResetPasswordEmailRequest> userResetPasswordEmailRequestValidator;
    private final ObjectValidator<UserResetPasswordCodeRequest> userResetPasswordCodeRequestValidator;
    private final ObjectValidator<UserChangePasswordRequest> userChangePasswordRequestValidator;
    private final ObjectValidator<UserDetailsUpdateRequest> userDetailsUpdateRequestValidator;
    private final ObjectValidator<UserCreateAdminRequest> userCreateAdminRequestValidator;
    private final ObjectValidator<UserDetailsUpdateAdminRequest> userDetailsUpdateAdminRequestValidator;
    private final ObjectValidator<UserActivateAccountRequest> userActivateAccountRequestValidator;
    private final ObjectValidator<CompleteRegistrationOAuth2UserRequest> completeRegistrationOAuth2UserRequestValidator;

    @Override
    public UserDTO save(UserCreateAdminRequest request) {
        userCreateAdminRequestValidator.validate(request);

        Optional<User> foundUser = userRepository.findByUsernameIgnoreCase(request.username());

        if (foundUser.isPresent()) {
            throw new ResourceAlreadyExistsException("This username is already taken");
        }

        Optional<User> foundUserByEmail = userRepository.findByEmailIgnoreCase(request.email());

        if (foundUserByEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("This email address is already taken");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();

        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setAddress(request.address());
        user.setIsEnabled(true);
        user.setIsLocked(false);
        user.setProvider(Provider.LOCAL);
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        Basket basket = new Basket();
        basket.setUser(savedUser);
        basket.setTotalPrice(0.0);
        basket.setPizzas(new ArrayList<>());

        basketRepository.save(basket);

        return userDTOMapper.apply(savedUser);
    }

    @Override
    public UserDTO save(UserSignUpRequest request) {
        userSignUpRequestValidator.validate(request);
        HashSet<String> validationErrors = new HashSet<>();

        Optional<User> foundUser = userRepository.findByUsernameIgnoreCase(request.username());

        if (foundUser.isPresent()) {
            throw new ResourceAlreadyExistsException("This username is already taken");
        }

        Optional<User> foundUserByEmail = userRepository.findByEmailIgnoreCase(request.email());

        if (foundUserByEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("This email address is already taken");
        }

        if (!request.password().matches(request.confirmPassword())) {
            validationErrors.add("The passwords do not match");
            throw new ObjectNotValidException(validationErrors);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername(request.username());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setAddress(request.address());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);
        user.setProvider(Provider.LOCAL);
        user.setIsLocked(false);
        user.setIsEnabled(false);

        String verificationToken = generateVerificationToken(user);

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
    public Map<String, String> signIn(UserSignInRequest request) {
        userSignInRequestValidator.validate(request);

        boolean rememberMe = Boolean.parseBoolean(request.rememberMe());

        User authenticatedUser = authenticate(request.username(), request.password());

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

        if (!foundUser.getProvider().equals(Provider.LOCAL)) {
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

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
    public UserDTO verifyByVerificationToken(UserActivateAccountRequest request) {
        userActivateAccountRequestValidator.validate(request);

        User foundUser = userRepository.findByVerificationToken(request.verificationCode())
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided verification code doesn't exist"));

        if (foundUser.getIsEnabled() || !foundUser.getProvider().equals(Provider.LOCAL)) {
            throw new ResourceAlreadyExistsException("The user is already verified");
        }

        if (foundUser.getVerificationToken().equals(request.verificationCode())) {
            foundUser.setIsEnabled(true);
            return userDTOMapper.apply(userRepository.save(foundUser));
        }

        else {
            throw new ResourceNotFoundException("The verification code is invalid");
        }
    }

    @Override
    public void resendConfirmationEmail(String email) {
        User foundUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exist"));

        if (foundUser.getIsEnabled() || !foundUser.getProvider().equals(Provider.LOCAL)) {
            throw new ResourceAlreadyExistsException("This user is already verified");
        }

        emailService.sendCompleteRegistrationEmail(foundUser.getEmail(), foundUser.getVerificationToken());
    }

    @Override
    public void sendResetPasswordEmail(UserResetPasswordEmailRequest request) {
        userResetPasswordEmailRequestValidator.validate(request);

        User foundUser = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided email doesn't exist"));

        foundUser.setResetPasswordToken(generateResetPasswordToken(foundUser));
        userRepository.save(foundUser);

        emailService.sendResetPasswordEmail(foundUser.getEmail(), foundUser.getResetPasswordToken());
    }

    @Override
    public UserDTO resetPasswordThroughToken(UserResetPasswordCodeRequest request) {
        userResetPasswordCodeRequestValidator.validate(request);

        HashSet<String> violationErrros = new HashSet<>();

        if (!request.newPassword().matches(request.confirmNewPassword())) {
            violationErrros.add("The passwords do not match");
            throw new ObjectNotValidException(violationErrros);
        }

        User foundUser = userRepository.findByResetPasswordToken(request.resetPasswordCode())
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided token doesn't exist"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        foundUser.setPassword(encoder.encode(request.newPassword()));

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO changePasswordById(Long userId, UserChangePasswordRequest request, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        HashSet<String> violationErrors = new HashSet<>();

        userChangePasswordRequestValidator.validate(request);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.currentPassword(), foundUser.getPassword())) {
            violationErrors.add("Invalid current password provided");
            throw new ObjectNotValidException(violationErrors);
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            violationErrors.add("The new passwords do not match");
            throw new ObjectNotValidException(violationErrors);
        }

        foundUser.setPassword(encoder.encode(request.newPassword()));
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
            imageService.delete(userImage);
            foundUser.setImage(null);
            return userDTOMapper.apply(userRepository.save(foundUser));
        }

        throw new ResourceNotFoundException("This user has no image");
    }

    @Override
    public UserDTO updateById(Long userId, UserDetailsUpdateRequest request, Authentication connectedUser) {
        HashSet<String> validationErrors = new HashSet<>();

        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        userDetailsUpdateRequestValidator.validate(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.password(), foundUser.getPassword())) {
            validationErrors.add("Invalid password provided");
            throw new ObjectNotValidException(validationErrors);
        }

        foundUser.setUsername(request.username());
        foundUser.setEmail(request.email());
        foundUser.setFirstName(request.firstName());
        foundUser.setLastName(request.lastName());
        foundUser.setAddress(request.address());

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public UserDTO updateById(Long userId, UserDetailsUpdateAdminRequest request) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        userDetailsUpdateAdminRequestValidator.validate(request);

        foundUser.setUsername(request.username());
        foundUser.setEmail(request.email());
        foundUser.setFirstName(request.firstName());
        foundUser.setLastName(request.lastName());
        foundUser.setAddress(request.address());

        return userDTOMapper.apply(userRepository.save(foundUser));
    }

    @Override
    public Map<String, String> completeRegistrationForOAuth2User(Long userId, CompleteRegistrationOAuth2UserRequest request, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        HashSet<String> validationErrors = new HashSet<>();

        completeRegistrationOAuth2UserRequestValidator.validate(request);

        if (!request.password().matches(request.confirmPassword())) {
            validationErrors.add("The passwords do not match");
            throw new ObjectNotValidException(validationErrors);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        foundUser.setFirstName(request.firstName());
        foundUser.setLastName(request.lastName());
        foundUser.setEmail(request.email());
        foundUser.setAddress(request.address());
        foundUser.setPassword(encoder.encode(request.password()));

        foundUser.setIsEnabled(true);

        userRepository.save(foundUser);

        String accessToken = jwtService.generateToken(foundUser, 1000L * 60 * 60); // 60 minutes = one hour
        String refreshToken = jwtService.generateToken(foundUser, 1000L * 60 * 60 * 24); // 24 hours = one day
        String rememberMeToken = jwtService.generateToken(foundUser, 1000L * 60 * 60 * 24 * 30); // 30 days = one month;

        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("rememberMeToken", rememberMeToken);

        return tokens;
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

    @Override
    public Boolean checkIfUserIsAdmin(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        return foundUser.getRole().equals(Role.ADMINISTRATOR);
    }

    @Override
    public Boolean checkIfUserIsEnabled(Long userId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        return foundUser.getIsEnabled();
    }

    @Override
    public Provider findUserProvider(Long userId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        return foundUser.getProvider();
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
