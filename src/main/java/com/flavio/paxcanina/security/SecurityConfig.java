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

/**
 * SecurityConfig
 * --------------
 * Main Spring Security configuration for the application.
 * - JWT stateless authentication
 * - Role-based endpoint protection (ADMIN / COACH / OWNER)
 * - Exposes public endpoints for auth, registration, and email validation
 */
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

    /** Password encoder (BCrypt) */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Dao-based authentication provider (standard best practice) */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder());
        auth.setUserDetailsService(userDetailsService);
        return auth;
    }

    /** Expose AuthenticationManager as a Bean */
    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Main filter chain for all HTTP requests.
     * - Public endpoints:
     *     /api/auth/**           (login, registration, password reset, etc)
     *     /api/validate-email/** (email verification with token)
     *     GET /api/breeds, /api/courses, /api/sessions, /api/age-groups (public data)
     * - Everything else requires authentication and specific role.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz

                        // --- AUTH & EMAIL VALIDATION (public) ---
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/validate-email/**").permitAll()

                        // --- BREEDS (public GET/OPTIONS, admin for others) ---
                        .requestMatchers(HttpMethod.GET, "/api/breeds", "/api/breeds/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/breeds", "/api/breeds/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/breeds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/breeds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/breeds/**").hasRole("ADMIN")

                        // --- COURSES (public GET/OPTIONS, admin for others) ---
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("ADMIN")

                        // --- AGE GROUPS (public GET/OPTIONS, admin/coach for others) ---
                        .requestMatchers(HttpMethod.GET, "/api/age-groups", "/api/age-groups/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/age-groups", "/api/age-groups/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")

                        // --- USERS MANAGEMENT (admin only) ---
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // --- SPECIALIZATIONS (admin only) ---
                        .requestMatchers("/api/specializations/**").hasRole("ADMIN")

                        // --- SESSIONS (public GET/OPTIONS, admin/coach for others) ---
                        .requestMatchers(HttpMethod.GET, "/api/sessions", "/api/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/sessions", "/api/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")

                        // --- SESSION REGISTRATION (admin + owner) ---
                        .requestMatchers(HttpMethod.POST, "/api/sessions/*/registration").hasAnyRole("ADMIN", "OWNER")

                        // --- DOGS (CRUD for admin and owner) ---
                        .requestMatchers(HttpMethod.GET, "/api/dogs", "/api/dogs/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers("/api/dogs/**").hasRole("ADMIN")

                        // --- ADMIN ENDPOINTS (admin only) ---
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- OWNER & COACH endpoints (any authenticated user) ---
                        .requestMatchers("/api/owners/**").authenticated()
                        .requestMatchers("/api/coaches/**").authenticated()

                        // --- CATCH ALL: authentication required ---
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Global CORS policy
     * - Allows requests from all origins (customize for production!)
     * - Allows all HTTP methods and headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // TODO: restrict in production!
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
