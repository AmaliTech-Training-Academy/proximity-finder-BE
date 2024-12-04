package team.proximity.helpandsupport.FAQGROUP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FAQGroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FAQGroupRepository faqGroupRepository;



    @BeforeEach
    void setUp() {

        entityManager.getEntityManager()
                .createNativeQuery("DELETE FROM faq")
                .executeUpdate();
        faqGroupRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    void shouldFindFAQGroupByName() {

        FAQGroup faqGroup = new FAQGroup();
        faqGroup.setName("test-group");
        entityManager.persist(faqGroup);
        entityManager.flush();
        entityManager.clear();


        Optional<FAQGroup> found = faqGroupRepository.findByName("test-group");


        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("test-group");
    }

    @Test
    void shouldReturnEmptyWhenGroupNameNotFound() {

        Optional<FAQGroup> notFound = faqGroupRepository.findByName("non-existent");

        assertThat(notFound).isEmpty();
    }
}
