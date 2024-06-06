package org.moldidev.moldispizza.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfiguration(JWTAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/authentication/**").permitAll()
                        .requestMatchers("/api/v1/pizzas/find-all").permitAll()
                        .requestMatchers("/api/v1/pizzas/find/**").permitAll()
                        .requestMatchers("/api/v1/reviews/find-all/pizza-id=**").permitAll()
                        .requestMatchers("/api/v1/users/verify/email=**/verification-token=**").permitAll()
                        .requestMatchers("/api/v1/users/update/username=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/users/update-password/user-id=**/old-password=**/new-password=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/users/update-email/user-id=**/new_email=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/reviews/save").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/reviews/update/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/reviews/delete/id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/baskets/find/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/baskets/add-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/baskets/remove-pizza/user-id=**/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/orders/find-all/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/orders/place-order/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/images/save").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/images/find/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/images/find/user-id=**/see-image").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers("/api/v1/images/update/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
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
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
