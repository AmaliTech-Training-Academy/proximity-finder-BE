package team.proximity.request_management.request_management.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    void existsByTitleAndAssignedProvider_ShouldReturnTrue_WhenQuoteExists() {
        Quote quote = Quote.builder()
                .title("Test Quote")
                .description("Test Description")
                .assignedProvider("provider1")
                .status(QuoteStatus.UNAPPROVED)
                .images(new ArrayList<>())
                .build();

        quoteRepository.save(quote);

        boolean exists = quoteRepository.existsByTitleAndAssignedProvider("Test Quote", "provider1");

        assertThat(exists).isTrue();
    }

    @Test
    void findByQuoteIdAndAssignedProvider_ShouldReturnQuote_WhenExists() {
        Quote quote = Quote.builder()
                .title("Test Quote")
                .description("Test Description")
                .assignedProvider("provider1")
                .status(QuoteStatus.UNAPPROVED)
                .images(new ArrayList<>())
                .build();

        quote = quoteRepository.save(quote);

        Optional<Quote> result = quoteRepository.findByQuoteIdAndAssignedProvider(
                quote.getQuoteId(), "provider1");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Quote");
        assertThat(result.get().getAssignedProvider()).isEqualTo("provider1");
    }

    @Test
    void findByQuoteIdAndCreatedBy_ShouldReturnQuote_WhenExists() {
        Quote quote = Quote.builder()
                .title("Test Quote")
                .description("Test Description")
                .createdBy("user1")
                .status(QuoteStatus.UNAPPROVED)
                .images(new ArrayList<>())
                .build();

        quote = quoteRepository.save(quote);

        Optional<Quote> result = quoteRepository.findByQuoteIdAndCreatedBy(
                quote.getQuoteId(), "user1");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Quote");
        assertThat(result.get().getCreatedBy()).isEqualTo("user1");
    }

    @Test
    void findByCreatedByWithDetails_ShouldReturnQuotesWithDetails() {
        Quote quote = Quote.builder()
                .title("Test Quote")
                .description("Test Description")
                .createdBy("user1")
                .status(QuoteStatus.UNAPPROVED)
                .images(new ArrayList<>())
                .build();

        QuoteImage image = new QuoteImage();
        image.setFilePath("test/image.jpg");
        image.setQuote(quote);
        quote.getImages().add(image);

        quoteRepository.save(quote);

        List<Quote> results = quoteRepository.findByCreatedByWithDetails("user1");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCreatedBy()).isEqualTo("user1");
        assertThat(results.get(0).getImages()).hasSize(1);
    }

    @Test
    void findByAssignedToWithDetails_ShouldReturnQuotesWithDetails() {
        Quote quote = Quote.builder()
                .title("Test Quote")
                .description("Test Description")
                .assignedProvider("provider1")
                .status(QuoteStatus.UNAPPROVED)
                .images(new ArrayList<>())
                .build();

        QuoteImage image = new QuoteImage();
        image.setFilePath("test/image.jpg");
        image.setQuote(quote);
        quote.getImages().add(image);

        quoteRepository.save(quote);

        List<Quote> results = quoteRepository.findByAssignedToWithDetails("provider1");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAssignedProvider()).isEqualTo("provider1");
        assertThat(results.get(0).getImages()).hasSize(1);
    }
}
