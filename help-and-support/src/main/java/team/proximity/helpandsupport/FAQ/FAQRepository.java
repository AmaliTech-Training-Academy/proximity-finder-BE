package team.proximity.helpandsupport.FAQ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;


import java.util.List;
import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQ, Long>, JpaSpecificationExecutor<FAQ> {

    List<FAQ> findByGroupId(Long groupId);
    Optional<FAQ> findByQuestionAndGroup(String question, FAQGroup group);
}
