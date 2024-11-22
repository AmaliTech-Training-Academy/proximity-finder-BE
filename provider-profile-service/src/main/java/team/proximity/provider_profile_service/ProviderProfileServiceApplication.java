package team.proximity.provider_profile_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;

import java.util.List;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ProviderProfileServiceApplication implements CommandLineRunner {

	private final PaymentPreferenceRepository paymentPreferenceRepository;

    public ProviderProfileServiceApplication(PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentPreferenceRepository = paymentPreferenceRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(ProviderProfileServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (paymentPreferenceRepository.count() == 0) {
			PaymentPreference bankAccount = new PaymentPreference("Bank Account");
			PaymentPreference mobileMoney = new PaymentPreference("Mobile Money");
			PaymentPreference payPal = new PaymentPreference("PayPal");

			paymentPreferenceRepository.saveAll(List.of(bankAccount, mobileMoney, payPal));
		}
	}
}
