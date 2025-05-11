package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

// После добавления зависимостей Spring Security пересоберите проект, чтобы устранить ошибку импорта.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/orders/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/orders/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/orders/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("MANAGER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/orders", true)
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user")
                .password("{noop}user")
                .roles("USER")
                .build(),
            User.withUsername("manager")
                .password("{noop}manager")
                .roles("MANAGER")
                .build()
        );
    }
} 