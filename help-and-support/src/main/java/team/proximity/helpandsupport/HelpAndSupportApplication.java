package team.proximity.helpandsupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class HelpAndSupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpAndSupportApplication.class, args);
	}

}
