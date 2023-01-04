package com.dev.week7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/h2-console").permitAll()
                        .requestMatchers("/swagger-ui").permitAll()
                        .requestMatchers("/actuator/metrics").authenticated()
                        .requestMatchers("/actuator/loggers").authenticated());
        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

}
