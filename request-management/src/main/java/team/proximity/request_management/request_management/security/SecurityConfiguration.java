package team.proximity.request_management.request_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationFilter authenticationFilter;

    public SecurityConfiguration(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/quotes")
                        .hasAuthority("ROLE_SEEKER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/quotes/creator",
                                "/api/v1/quotes/{quoteId}/creator/details")
                        .hasAuthority("ROLE_SEEKER")


                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/quotes/{quoteId}/status/approve",
                                "/api/v1/quotes/{quoteId}/status/decline")
                        .hasAuthority("ROLE_PROVIDER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/quotes/provider",
                                "/api/v1/requests/assigned",
                                "/api/v1/quotes/{quoteId}/provider/details")
                        .hasAuthority("ROLE_PROVIDER")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AppAuthenticationEntryPoint())
                        .accessDeniedHandler(new AppAccessDeniedHandler())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(authenticationFilter, AuthorizationFilter.class)
                .build();
    }

}
