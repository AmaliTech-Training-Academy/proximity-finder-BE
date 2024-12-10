package team.proximity.helpandsupport.security;

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
import team.proximity.helpandsupport.exception.AppAccessDeniedHandler;
import team.proximity.helpandsupport.exception.AppAuthenticationEntryPoint;

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
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/support/faqs",
                                "/api/v1/support/faqs/**",
                                "/api/v1/support/faq-groups",
                                "/api/v1/support/faq-groups/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/support/contact-support").permitAll()

                        .requestMatchers(HttpMethod.POST,

                                "/api/v1/support/faqs",
                                "/api/v1/support/faq-groups")
                        .hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/support/faqs/**",
                                "/api/v1/support/faq-groups/**")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/support/faqs/**",
                                "/api/v1/support/faq-groups/**")
                        .hasAuthority("ROLE_ADMIN")

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
