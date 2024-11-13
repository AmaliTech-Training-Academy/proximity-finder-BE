package team.proximity.provider_profile_service.about;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Transactional
class AboutRepositoryTest {

    @Autowired
    private AboutRepository aboutRepository;

    private About about;

    @BeforeEach
    public void setUp() {

        about = About.builder()
                .inceptionDate(LocalDate.of(2020, 1, 1))
                .socialMediaLinks(Set.of("https://facebook.com", "https://twitter.com"))
                .numberOfEmployees(100)
                .businessSummary("Business Summary")
                .build();


        aboutRepository.save(about);
    }

    @Test
    public void testFindByInceptionDateWhenExistsShouldReturnAbout() {

        LocalDate inceptionDate = about.getInceptionDate();
        Optional<About> foundAbout = aboutRepository.findByInceptionDate(inceptionDate);

        assertThat(foundAbout).isPresent();
        assertThat(foundAbout.get().getInceptionDate()).isEqualTo(inceptionDate);
    }

    @Test
    public void testFindByInceptionDateWhenNotExistsShouldReturnEmpty() {

        LocalDate nonExistentDate = LocalDate.of(2021, 1, 1);
        Optional<About> foundAbout = aboutRepository.findByInceptionDate(nonExistentDate);

        assertThat(foundAbout).isEmpty();
    }
}