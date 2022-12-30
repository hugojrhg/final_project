package com.dev.week7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User
                .withDefaultPasswordEncoder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build());

        manager.createUser(User
                .withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build());

        return manager;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/users/delete/**").hasRole("ADMIN")
                        .requestMatchers("/users/update/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin().and()
                .httpBasic(withDefaults());

        http.csrf().disable();
        http.headers().frameOptions().disable();
        return http.build();

    }

}
