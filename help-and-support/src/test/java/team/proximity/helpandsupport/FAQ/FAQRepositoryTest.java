package team.proximity.helpandsupport.FAQ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FAQRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FAQRepository faqRepository;

    private FAQGroup faqGroup;
    private FAQ faq1;
    private FAQ faq2;

    @BeforeEach
    void setUp() {

        faqGroup = new FAQGroup();
        faqGroup.setName("General");
        entityManager.persist(faqGroup);


        faq1 = new FAQ();
        faq1.setQuestion("What is Proximity?");
        faq1.setAnswer("A platform for connecting people");
        faq1.setGroup(faqGroup);
        entityManager.persist(faq1);

        faq2 = new FAQ();
        faq2.setQuestion("How to use Proximity?");
        faq2.setAnswer("Follow the user guide");
        faq2.setGroup(faqGroup);
        entityManager.persist(faq2);

        entityManager.flush();
    }

    @Test
    void shouldFindFAQsByGroupId() {

        List<FAQ> foundFAQs = faqRepository.findByGroupId(faqGroup.getId());


        assertThat(foundFAQs).hasSize(2);
        assertThat(foundFAQs).contains(faq1, faq2);
    }

    @Test
    void shouldFindFAQByQuestionAndGroup() {

        Optional<FAQ> foundFAQ = faqRepository.findByQuestionAndGroup(
                "What is Proximity?",
                faqGroup
        );

        assertThat(foundFAQ).isPresent();
        assertThat(foundFAQ.get().getQuestion()).isEqualTo(faq1.getQuestion());
        assertThat(foundFAQ.get().getAnswer()).isEqualTo(faq1.getAnswer());
    }

    @Test
    void shouldReturnEmptyWhenQuestionNotFound() {

        Optional<FAQ> notFoundFAQ = faqRepository.findByQuestionAndGroup(
                "Non-existent question",
                faqGroup
        );

        assertThat(notFoundFAQ).isEmpty();
    }
}
