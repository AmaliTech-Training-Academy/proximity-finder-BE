package team.proximity.provider_profile_service.about;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class AboutRepositoryTest {

    @Autowired
    private AboutRepository aboutRepository;

    @Test
    void testFindByInceptionDate() {

        LocalDate inceptionDate = LocalDate.now();
        About about = new About();
        about.setInceptionDate(inceptionDate);
        aboutRepository.save(about);


        Optional<About> result = aboutRepository.findByInceptionDate(inceptionDate);


        assertTrue(result.isPresent());
        assertEquals(inceptionDate, result.get().getInceptionDate());
    }

    @Test
    void testFindByCreatedBy() {

        String createdBy = "test-user";
        About about = new About();
        about.setCreatedBy(createdBy);
        aboutRepository.save(about);

        Optional<About> result = aboutRepository.findByCreatedBy(createdBy);


        assertTrue(result.isPresent());
        assertEquals(createdBy, result.get().getCreatedBy());
    }

}