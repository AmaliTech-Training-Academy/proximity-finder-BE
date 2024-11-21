package team.proximity.provider_profile_service.bank;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {

   Optional< Bank> findByBankName(String name);
}
