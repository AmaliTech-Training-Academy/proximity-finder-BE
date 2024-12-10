package team.proximity.management.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializationConfig {
    @Autowired
    private ServicesSeeder servicesSeeder;

    @Autowired
    private ProviderSeeder providerServiceSeeder;

    @Bean
    public ApplicationRunner initializeDatabase() {
        return args -> {
            servicesSeeder.seedServices();
            providerServiceSeeder.seedProviderServices();
        };
    }
}
