package com.flavio.paxcanina.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder());
        auth.setUserDetailsService(userDetailsService);
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz

                        // 1. Auth: Login, Registration
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Courses (GET/OPTIONS public, CRUD admin)
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("ADMIN")

                        // 3. Age Groups (GET public, CRUD admin/coach)
                        .requestMatchers(HttpMethod.GET, "/api/age-groups", "/api/age-groups/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/age-groups", "/api/age-groups/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")

                        // 4. Users management (admin only)
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // 5. Specializations (admin only)
                        .requestMatchers("/api/specializations/**").hasRole("ADMIN")

                        // 6. Sessions (GET public, CRUD admin/coach)
                        .requestMatchers(HttpMethod.GET, "/api/sessions", "/api/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/sessions", "/api/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")

                        // 7. Session registration (admin + owner)
                        .requestMatchers(HttpMethod.POST, "/api/sessions/*/registration").hasAnyRole("ADMIN", "OWNER")

                        // 8. Dogs (CRUD admin only)
                        .requestMatchers("/api/dogs/**").hasRole("ADMIN")

                        // 9. Admin endpoints (admin only)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 10. Owner and Coach endpoints (any authenticated)
                        .requestMatchers("/api/owners/**").authenticated()
                        .requestMatchers("/api/coaches/**").authenticated()

                        // 11. Fallback: authentication required
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
