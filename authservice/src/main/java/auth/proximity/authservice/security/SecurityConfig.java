package auth.proximity.authservice.security;

import auth.proximity.authservice.security.jwt.JWTAuthenticationFilter;
import auth.proximity.authservice.security.jwt.JWTAuthorizationFilter;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureCSRF(http);
        configureHttpRequests(http);
        configureAuthenticationFilter(http);
        configureAuthorizationFilter(http);
        return http.build();
    }

    private void configureCSRF(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureHttpRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests)
                ->  requests
                .requestMatchers("/api/auth/public/**").permitAll()
                .anyRequest().authenticated());
    }

    private void configureAuthenticationFilter(HttpSecurity http) throws Exception {
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtils));
    }

    private void configureAuthorizationFilter(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JWTAuthorizationFilter(jwtUtils, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
