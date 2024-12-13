package team.proximity.helpandsupport.FAQGROUP;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FAQGroupRepository extends JpaRepository<FAQGroup, Long> {

    Optional<FAQGroup> findByName(String name);
}
