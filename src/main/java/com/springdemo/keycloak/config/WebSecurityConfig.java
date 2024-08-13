package com.springdemo.keycloak.config;

import com.springdemo.keycloak.constants.AuthConstants;
import com.springdemo.keycloak.utils.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/api/v1").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v2").hasRole(AuthConstants.ADMIN);
                    auth.requestMatchers(HttpMethod.GET, "/api/v3").hasRole(AuthConstants.USER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v4").hasAnyRole(AuthConstants.ADMIN, AuthConstants.USER);
                    auth.anyRequest().authenticated();
                });

        httpSecurity
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter));
                });
        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.disable();
                });

        return httpSecurity.build();
    }
}
