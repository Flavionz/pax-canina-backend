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

                        // 1) Public authentication endpoints (login, registration)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2) Course endpoints: GET requests are public, other methods restricted to ADMIN
                        .requestMatchers(HttpMethod.GET,  "/api/course/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/course/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/course/**").hasRole("ADMIN")

                        // 3) User management endpoints: ADMIN only
                        .requestMatchers("/api/user/**").hasRole("ADMIN")

                        // 4) Specialization management: ADMIN only
                        .requestMatchers("/api/specialization/**").hasRole("ADMIN")

                        // 5) Session endpoints: GET requests are public
                        .requestMatchers(HttpMethod.GET,  "/api/session/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/session/**").permitAll()

                        // 6) Session creation, update, and deletion: restricted to ADMIN and COACH
                        .requestMatchers(HttpMethod.POST,   "/api/session/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.PUT,    "/api/session/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE, "/api/session/**").hasAnyRole("ADMIN", "COACH")

                        // 7) Dog registration to session: allowed for ADMIN (all dogs) and OWNER (only their own dogs)
                        .requestMatchers(HttpMethod.POST, "/api/session/*/registration").hasAnyRole("ADMIN", "OWNER")

                        // 8) Dog management endpoints: ADMIN only (CRUD). Owner-specific endpoints are under /api/owner/**
                        .requestMatchers("/api/dogs/**").hasRole("ADMIN")

                        // 9) Admin-specific endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 10) Owner endpoints: authenticated users only (role-based logic in controllers/services)
                        .requestMatchers("/api/owner/**").authenticated()

                        // 11) Coach endpoints: authenticated users only (role-based logic in controllers/services)
                        .requestMatchers("/api/coach/**").authenticated()

                        // 12) Any other endpoints: authentication required
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
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
