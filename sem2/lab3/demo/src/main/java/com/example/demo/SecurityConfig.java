package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123")
                .roles("USER")
                .build();
        UserDetails moderator = User.withDefaultPasswordEncoder()
                .username("moderator")
                .password("123")
                .roles("MODERATOR")
                .build();
        UserDetails editor = User.withDefaultPasswordEncoder()
                .username("editor")
                .password("123")
                .roles("EDITOR")
                .build();
        return new InMemoryUserDetailsManager(user, moderator, editor);
    }
}
