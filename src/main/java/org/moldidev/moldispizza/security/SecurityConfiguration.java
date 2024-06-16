package org.moldidev.moldispizza.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users?username=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/verify?email=**&token=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/pizzas**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/pizza-id=**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews?id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews?id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/baskets/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/baskets?id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/images/user-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/images/pizza-id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/images").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/images?id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/images?id=**").hasAnyRole("CUSTOMER", "ADMINISTRATOR")

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
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
