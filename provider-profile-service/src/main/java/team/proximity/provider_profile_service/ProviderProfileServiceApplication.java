package team.proximity.provider_profile_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
@OpenAPIDefinition(
		info = @Info(
				title = "provider profile service",
				description = "providers wil provide their information",
				version = "2.3"


		)
)
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ProviderProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderProfileServiceApplication.class, args);
	}

}
