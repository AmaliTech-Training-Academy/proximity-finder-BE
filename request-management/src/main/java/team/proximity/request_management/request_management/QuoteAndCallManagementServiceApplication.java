package team.proximity.request_management.request_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class QuoteAndCallManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteAndCallManagementServiceApplication.class, args);
	}

}
