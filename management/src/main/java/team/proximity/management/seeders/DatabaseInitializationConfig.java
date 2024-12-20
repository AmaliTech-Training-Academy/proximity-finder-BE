package team.proximity.management.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class DatabaseInitializationConfig {
    @Autowired
    private ServicesSeeder servicesSeeder;

    @Autowired
    private ProviderSeeder providerServiceSeeder;

    @Value("${app.seed-db:false}") // Default to false if property is not set
    private boolean seedDatabase;

    @Bean
    public ApplicationRunner initializeDatabase() {
        return args -> {
            if (seedDatabase) {
                System.out.println("Seeding the database...");
                servicesSeeder.seedServices();
                providerServiceSeeder.seedProviderServices();
            } else {
                System.out.println("Database seeding is disabled.");
            }
        };
    }
}
