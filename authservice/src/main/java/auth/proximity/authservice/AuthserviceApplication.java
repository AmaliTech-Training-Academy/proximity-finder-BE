package auth.proximity.authservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "User Authentication REST API Documentation",
				description = "Proximity Finder User Authentication microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "MichaelSamuelAhmed",
						email = "msa@amalitech",
						url = "https://amalitech.org"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Proximity Finder User Authentication microservice REST API Documentation",
				url = "http://localhost:8080/swagger-ui/index.html"
		)
)
@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

}
