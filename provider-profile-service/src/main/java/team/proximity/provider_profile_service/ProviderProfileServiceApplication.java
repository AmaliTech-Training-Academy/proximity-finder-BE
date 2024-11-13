package team.proximity.provider_profile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ProviderProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderProfileServiceApplication.class, args);
	}

}
