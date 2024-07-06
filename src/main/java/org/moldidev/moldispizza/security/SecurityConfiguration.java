package org.moldidev.moldispizza.security;

import lombok.RequiredArgsConstructor;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users/verify**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/username=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/remove-image/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/set-image/id=**/image-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/resend-confirmation-email/email=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/send-reset-password-token/email=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/reset-password/reset-password-token=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/change-password/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/pizzas**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/pizza-id=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/baskets/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/baskets/add-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/baskets/remove-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

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
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
