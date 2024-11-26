package team.proximity.provider_profile_service.bank;

import org.springframework.stereotype.Component;

@Component
public class BankMapper {

    public BankResponse mapToResponse(Bank bank) {
        return new BankResponse(
                bank.getBankName()
        );
    }
}
