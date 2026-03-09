package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import({UsersAuthEntryIntegrationTest.AuthOnlySecurityConfig.class,
        UsersAuthEntryIntegrationTest.TestBeans.class})
class UsersAuthEntryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void users_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Configuration
    static class AuthOnlySecurityConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/users/**").authenticated()
                            .anyRequest().permitAll()
                    )
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                    ))
                    .anonymous(anon -> {});
            return http.build();
        }
    }

    @Configuration
    static class TestBeans {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }
}
