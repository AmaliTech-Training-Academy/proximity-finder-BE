package team.proximity.provider_profile_service.bank;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private final Logger LOGGER = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankMapper bankMapper;

    private final BankRepository bankRepository;

    public BankServiceImpl(BankMapper bankMapper, BankRepository bankRepository) {
        this.bankMapper = bankMapper;
        this.bankRepository = bankRepository;
    }

    public List<BankResponse> getAllBanks() {


        LOGGER.info("Retrieving all banks");

        return bankRepository.findAll()
                .stream()
                .map(bankMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
