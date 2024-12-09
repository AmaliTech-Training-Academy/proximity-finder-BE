package auth.proximity.authservice.config;

import auth.proximity.authservice.repository.UserRepository;
import auth.proximity.authservice.security.OAuth2AuthenticationFailureHandler;
import auth.proximity.authservice.security.OAuth2AuthenticationSuccessHandler;
import auth.proximity.authservice.security.jwt.JWTAuthorizationFilter;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.services.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        configureCSRF(http);
        configureHttpRequests(http);
        configureAuthorizationFilter(http);
        configureOAuth2Login(http);
        return http.build();
    }

    private void configureCSRF(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureHttpRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests)
                ->  requests
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/auth/public/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/v1/password/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/**").permitAll()
                .requestMatchers("/api/v1/**").permitAll()
                .anyRequest().authenticated());
    }

    private void configureAuthorizationFilter(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JWTAuthorizationFilter(jwtUtils, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);
    }

    private void configureOAuth2Login(HttpSecurity http) throws Exception {
       http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
        );
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
