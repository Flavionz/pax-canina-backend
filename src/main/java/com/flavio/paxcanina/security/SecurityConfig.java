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

                        // 1) Auth endpoints (login, register): public
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2) Courses: GET open to all, rest only for ADMIN
                        .requestMatchers(HttpMethod.GET,  "/api/course/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/course/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/course/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/course/**").hasRole("ADMIN")

                        // 3) Users CRUD: only ADMIN
                        .requestMatchers("/api/user/**").hasRole("ADMIN")

                        // 4) Specializations: only ADMIN
                        .requestMatchers("/api/specialization/**").hasRole("ADMIN")

                        // 5) Sessions: only ADMIN
                        .requestMatchers("/api/session/**").hasRole("ADMIN")

                        // 6) Dogs: only ADMIN (for CRUD). Public/owner endpoints will be in /api/owner/**
                        .requestMatchers("/api/dog/**").hasRole("ADMIN")

                        // 7) Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 8) Owner endpoints: authenticated (can be further limited by roles in controller)
                        .requestMatchers("/api/owner/**").authenticated()

                        // 9) Coach endpoints (se previsti)
                        .requestMatchers("/api/coach/**").authenticated()

                        // 10) Any other endpoint: authentication required
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
