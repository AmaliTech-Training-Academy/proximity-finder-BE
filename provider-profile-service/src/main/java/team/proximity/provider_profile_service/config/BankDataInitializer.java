package team.proximity.provider_profile_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.provider_profile_service.bank.Bank;
import team.proximity.provider_profile_service.bank.BankRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BankDataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner loadBanks(BankRepository bankRepository) {
        return args -> {
            List<String> bankNames = Arrays.asList(
                    "ACCESS BANK GHANA",
                    "ABSA BANK GHANA LIMITED",
                    "ADB BANK",
                    "ARB APEX BANK LTD.",
                    "BANK OF AFRICA",
                    "BANK OF GHANA",
                    "CAL BANK LTD",
                    "CONSOLIDATED BANK GHANA LTD",
                    "ECOBANK GHANA LIMITED",
                    "FIDELITY BANK GHANA LIMITED",
                    "FIRST ATLANTIC BANK LIMITED",
                    "FBN BANK (GHANA) LIMITED",
                    "FIRST NATIONAL BANK GHANA LIMITED",
                    "GCB BANK LIMITED",
                    "GUARANTY TRUST BANK (GHANA) LTD",
                    "NATIONAL INVESTMENT BANK LIMITED",
                    "OMNI-BSIC BANK GHANA LIMITED.",
                    "PRUDENTIAL BANK LTD.",
                    "REPUBLIC BANK (GHANA) LTD",
                    "STANBIC BANK GHANA LTD",
                    "STANDARD CHARTERED BANK GHANA LTD",
                    "SOCIETE GENERALE",
                    "UNITED BANK FOR AFRICA",
                    "UNIVERSAL MERCHANT BANK GHANA LIMITED",
                    "ZENITH BANK (GH) LTD"
            );

            long bankCount = bankRepository.count();
            if (bankCount == 0) {

                bankNames.forEach(bankName -> {
                    Bank bank = new Bank();
                    bank.setBankName(bankName);
                    bankRepository.save(bank);
                });

            }
        };
    }
}
