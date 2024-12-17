package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;

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

    @Override
    public PaymentMethod update(PaymentMethod existing, PaymentMethodRequest request) {
        BankPayment bankPayment = (BankPayment) existing;

        return getBankPayment(request, bankPayment);
    }

    private BankPayment getBankPayment(PaymentMethodRequest request, BankPayment bankPayment) {
        if (StringUtils.hasText(request.bankName())) {
            Bank bank = bankRepository.findByBankName(request.bankName())
                    .orElseThrow(() -> new PaymentMethodCreationException("Bank does not exist."));
            bankPayment.setBankName(bank.getBankName());
        }
        if (StringUtils.hasText(request.accountNumber())) {
            bankPayment.setAccountNumber(request.accountNumber());
        }
        if (StringUtils.hasText(request.accountAlias())) {
            bankPayment.setAccountAlias(request.accountAlias());
        }
        if (StringUtils.hasText(request.accountName())) {
            bankPayment.setAccountName(request.accountName());
        }
        return bankPayment;
    }
}
