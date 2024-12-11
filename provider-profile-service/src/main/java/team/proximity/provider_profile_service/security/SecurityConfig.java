package team.proximity.provider_profile_service.security;

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
import team.proximity.provider_profile_service.exception.auth.AppAccessDeniedHandler;
import team.proximity.provider_profile_service.exception.auth.AppAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/provider-service/about/provider-profile")
                        .hasAnyAuthority("ROLE_PROVIDER", "ROLE_SEEKER", "ROLE_ADMIN")


                        .requestMatchers(
                                "/api/v1/provider-service/payment-method")
                        .hasAnyAuthority("ROLE_PROVIDER", "ROLE_SEEKER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/provider-service/about/provider-profile/**"
                        )
                        .hasAnyAuthority("ROLE_PROVIDER","ROLE_SEEKER", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "api/v1/provider-service/payment-method/providers/mobile-money-providers",
                                "api/v1/provider-service/payment-preferences"
                        )
                        .permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AppAuthenticationEntryPoint())
                        .accessDeniedHandler(new AppAccessDeniedHandler())
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .build();
    }

}