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
 * - JWT stateless authentication
 * - Role-based endpoint protection
 * - Public endpoints for registration, login, email verification, password reset
 * - Strict CORS for development (localhost) and production domain
 *
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

    /** BCrypt password encoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Authentication provider (DAO-based) */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder());
        auth.setUserDetailsService(userDetailsService);
        return auth;
    }

    /** Expose AuthenticationManager as Bean */
    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /** Main security filter chain */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz

                        /* ======================
                           CORS preflight (always allow)
                           ====================== */
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        /* ======================
                           PUBLIC AUTH ENDPOINTS
                           ====================== */
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/register/**",
                                "/api/auth/password-reset-request",
                                "/api/auth/password-reset"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/auth/verify-email").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/resend-verification/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        /* ======================
                           PASSWORD CHANGE (authenticated)
                           ====================== */
                        .requestMatchers(HttpMethod.POST, "/api/auth/change-password").authenticated()

                        /* ======================
                           BREEDS (GET public, CRUD admin)
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/breeds", "/api/breeds/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/breeds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/breeds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/breeds/**").hasRole("ADMIN")

                        /* ======================
                           COURSES (GET public, CRUD admin)
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/courses/for-coach/me").hasRole("COACH")
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/courses/**").hasRole("ADMIN")

                        /* ======================
                           AGE GROUPS (GET public, CRUD admin/coach)
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/age-groups", "/api/age-groups/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT,  "/api/age-groups/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE,"/api/age-groups/**").hasAnyRole("ADMIN", "COACH")

                        /* ======================
                           USERS (admin only)
                           ====================== */
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        /* ======================
                           SPECIALIZATIONS
                           - GET public (used on homepage/forms)
                           - Write ops restricted to admin
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/specializations", "/api/specializations/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/specializations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/specializations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/specializations/**").hasRole("ADMIN")

                        /* ======================
                           SESSION REGISTRATION
                           ====================== */
                        .requestMatchers(HttpMethod.POST, "/api/sessions/*/registration").hasAnyRole("ADMIN", "OWNER")

                        /* ======================
                           SESSIONS (GET public, CRUD admin/coach)
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/sessions", "/api/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT,  "/api/sessions/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE,"/api/sessions/**").hasAnyRole("ADMIN", "COACH")

                        /* ======================
                           DOGS (admin/owner)
                           ====================== */
                        .requestMatchers(HttpMethod.GET, "/api/dogs", "/api/dogs/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT,  "/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE,"/api/dogs/me/**").hasRole("OWNER")
                        .requestMatchers("/api/dogs/**").hasRole("ADMIN")

                        /* ======================
                           ROLE-SPECIFIC AREAS
                           (prefix fix: singular)
                           ====================== */
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/owner/**").authenticated()
                        .requestMatchers("/api/coach/**").authenticated()

                        /* ======================
                           EVERYTHING ELSE
                           ====================== */
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** Global CORS policy */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Development
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        // Production example:
        // config.setAllowedOrigins(List.of("https://www.paxcanina.fr"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
