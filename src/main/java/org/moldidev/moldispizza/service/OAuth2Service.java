package org.moldidev.moldispizza.service;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.Provider;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.oauth2.CustomOAuth2User;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final BasketRepository basketRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${secure-password}")
    private String securePassword;

    public String signInSuccess(Map<String, Object> attributes) {
        Optional<User> foundUserByEmail = userRepository.findByEmailIgnoreCase((String) attributes.get("email"));

        if (foundUserByEmail.isPresent() && foundUserByEmail.get().getProvider().equals(Provider.GOOGLE) && foundUserByEmail.get().getIsEnabled()) {
            String accessToken = jwtService.generateToken(foundUserByEmail.get(), 1000L * 60 * 60); // 60 minutes = one hour
            String refreshToken = jwtService.generateToken(foundUserByEmail.get(), 1000L * 60 * 60 * 24); // 24 hours = one day
            String rememberMeToken = jwtService.generateToken(foundUserByEmail.get(), 1000L * 60 * 60 * 24 * 30); // 30 days = one month

            return String.format("%s/pizzas?accessToken=%s&refreshToken=%s&rememberMeToken=%s", frontendUrl, accessToken, refreshToken, rememberMeToken);
        }

        else if (foundUserByEmail.isPresent() && !foundUserByEmail.get().getProvider().equals(Provider.GOOGLE)) {
            return frontendUrl + "/sign-in?error=This account is already registered using the standard registration method. Sign in by using the username and password";
        }

        else if (foundUserByEmail.isPresent() && foundUserByEmail.get().getProvider().equals(Provider.GOOGLE) && !foundUserByEmail.get().getIsEnabled()) {
            String accessToken = jwtService.generateToken(foundUserByEmail.get(), 1000L * 60 * 10); // 10 minutes
            return String.format("%s/complete-registration-oauth2-user?accessToken=%s", frontendUrl, accessToken);
        }

        else if (foundUserByEmail.isEmpty()) {
            User user = registerNewUser(attributes);
            String accessToken = jwtService.generateToken(user, 1000L * 60 * 10); // 10 minutes
            return String.format("%s/complete-registration-oauth2-user?accessToken=%s", frontendUrl, accessToken);
        }

        return null;
    }

    public String signInFailure() {
        return frontendUrl + "/sign-in?error=Authentication failed. Please try to sign-in using another way or try again later!";
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();

            if (!user.getProvider().equals(Provider.GOOGLE)) {
                throw new OAuth2AuthenticationException("This account is already registered using a different provider.");
            }
        }

        else {
            user = registerNewUser(attributes);
        }

        return new CustomOAuth2User(oAuth2User, user);
    }

    private User registerNewUser(Map<String, Object> attributes) {
        User user = new User();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setUsername((String) attributes.get("name"));
        user.setFirstName((String) attributes.get("given_name"));
        user.setLastName((String) attributes.get("family_name"));
        user.setEmail((String) attributes.get("email"));
        user.setAddress("!!! TODO !!! UPDATE YOUR ADDRESS !!!");
        user.setPassword(encoder.encode(securePassword));
        user.setIsLocked(false);
        user.setIsEnabled(false);
        user.setProvider(Provider.GOOGLE);
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        Basket basket = new Basket();
        basket.setUser(savedUser);
        basket.setPizzas(new ArrayList<>());
        basket.setTotalPrice(0.0);
        basketRepository.save(basket);

        return savedUser;
    }
}
