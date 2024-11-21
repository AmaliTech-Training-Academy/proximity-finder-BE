package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.bank.Bank;
import team.proximity.provider_profile_service.bank.BankRepository;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;


@Component("BANK ACCOUNT")
public class BankPaymentCreator implements PaymentMethodCreator {
    private final BankRepository bankRepository;

    public BankPaymentCreator(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }


    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        if (request.bankName() == null || request.accountName() == null || request.bankName().isBlank()) {
            throw new PaymentMethodCreationException("All fields are required: bankName, accountName, accountNumber");
        }
        Bank bank = bankRepository.findByBankName(request.bankName())
                .orElseThrow(() -> new PaymentMethodCreationException("Bank with name '" + request.bankName() + "' does not exist"));

        BankPayment bankPayment = new BankPayment();
        bankPayment.setBankName(bank.getBankName());
        bankPayment.setAccountName(request.accountName());
        bankPayment.setAccountAlias(request.accountAlias());
        bankPayment.setAccountNumber(request.accountNumber());
        return bankPayment;
    }
}
