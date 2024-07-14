package org.moldidev.moldispizza.security;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.oauth2.OAuth2SignInFailureHandler;
import org.moldidev.moldispizza.oauth2.OAuth2SignInSuccessHandler;
import org.moldidev.moldispizza.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2SignInSuccessHandler oAuth2SignInSuccessHandler;
    private final OAuth2SignInFailureHandler oAuth2SignInFailureHandler;
    private final OAuth2Service oAuth2Service;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**").permitAll()
                        .requestMatchers("/oauth2/authorization/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users/enabled/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/username=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/provider/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/complete-registration-oauth2-user/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/verify").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/remove-image/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/set-image/id=**/image-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/resend-confirmation-email/email=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/send-reset-password-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/change-password/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/pizzas**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/pizzas/id=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/exists/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/pizza-id=**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/baskets/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/baskets/add-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/baskets/remove-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/exists/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/set-paid/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/pay-pending-order/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/images/user-id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/images/pizza-id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/images/id=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/images").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .anyRequest().hasRole("ADMINISTRATOR")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SignInSuccessHandler)
                        .failureHandler(oAuth2SignInFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service)
                        )
                )
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
