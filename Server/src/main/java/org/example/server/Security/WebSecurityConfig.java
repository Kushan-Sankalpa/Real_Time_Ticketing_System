package org.example.server.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class WebSecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * @param http The HttpSecurity object for configuring web security.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there are errors during configuration.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection
                .csrf(csrf -> csrf.disable())

                // Enables CORS (Cross-Origin Resource Sharing) with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configures URL-based access control
                .authorizeHttpRequests(auth -> auth
                        // Allows unrestricted access to H2 console

                        .requestMatchers("/h2-console/**").permitAll()
                        // Allows unrestricted access to API and WebSocket endpoints
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()


                        // Requires authentication for all other endpoints
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    /**
     * Configures CORS settings to allow cross-origin requests from specific origins.
     *
     * @return The configured CorsConfigurationSource.
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5174"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
