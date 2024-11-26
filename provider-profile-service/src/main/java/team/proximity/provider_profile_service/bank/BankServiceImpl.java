package team.proximity.provider_profile_service.bank;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private final BankMapper bankMapper;

    private final BankRepository bankRepository;

    public BankServiceImpl(BankMapper bankMapper, BankRepository bankRepository) {
        this.bankMapper = bankMapper;
        this.bankRepository = bankRepository;
    }

    public List<BankResponse> getAllBanks() {

        return bankRepository.findAll()
                .stream()
                .map(bankMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
