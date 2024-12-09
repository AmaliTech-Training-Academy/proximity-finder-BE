package team.proximity.request_management.request_management.quotes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team.proximity.request_management.request_management.descision.QuoteDecision;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteMapperTest {

    private QuoteMapper quoteMapper;

    @BeforeEach
    void setUp() {
        quoteMapper = new QuoteMapper();
    }

    @Test
    void mapToQuote_ShouldMapBasicFields() {

        QuoteRequest request = new QuoteRequest(
                "Test Title",
                "Test Description",
                "Additional Details",
                "Test Location",
                "January 01, 2024",
                "09:00",
                "January 02, 2024",
                "17:00",
                "provider1",
                null
        );


        Quote result = quoteMapper.mapToQuote(request);


        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getLocation()).isEqualTo("Test Location");
        assertThat(result.getStartDate()).isEqualTo("January 01, 2024");
        assertThat(result.getEndDate()).isEqualTo("January 02, 2024");
        assertThat(result.getStartTime()).isEqualTo("09:00");
        assertThat(result.getEndTime()).isEqualTo("17:00");
        assertThat(result.getAdditionalDetails()).isEqualTo("Additional Details");
        assertThat(result.getStatus()).isEqualTo(QuoteStatus.UNAPPROVED);
        assertThat(result.getAssignedProvider()).isEqualTo("provider1");
    }

    @Test
    void mapToQuoteResponse_ShouldMapAllFields() {

        Quote quote = createSampleQuote();
        addSampleImages(quote);
        addSampleDecision(quote);


        QuoteResponse response = quoteMapper.mapToQuoteResponse(quote);


        assertThat(response.quoteId()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.description()).isEqualTo("Test Description");
        assertThat(response.location()).isEqualTo("Test Location");
        assertThat(response.status()).isEqualTo("UNAPPROVED");
        assertThat(response.images()).hasSize(1);
        assertThat(response.images().get(0)).isEqualTo("test/path/image.jpg");


        assertThat(response.decision()).isNotNull();
        assertThat(response.decision().price()).isEqualTo(BigDecimal.valueOf(100.0));
        assertThat(response.decision().approvalDetails()).isEqualTo("Approved");
    }

    private Quote createSampleQuote() {
        return Quote.builder()
                .quoteId(1L)
                .title("Test Title")
                .description("Test Description")
                .location("Test Location")
                .status(QuoteStatus.UNAPPROVED)
                .startDate("January 01, 2024")
                .endDate("January 02, 2024")
                .startTime("09:00")
                .endTime("17:00")
                .createdBy("user1")
                .assignedProvider("provider1")
                .images(new ArrayList<>())
                .build();
    }

    private void addSampleImages(Quote quote) {
        QuoteImage image = new QuoteImage();
        image.setFilePath("test/path/image.jpg");
        image.setQuote(quote);
        quote.getImages().add(image);
    }

    private void addSampleDecision(Quote quote) {
        QuoteDecision decision = new QuoteDecision();
        decision.setPrice(BigDecimal.valueOf(100.0));
        decision.setApprovalDetails("Approved");
        decision.setQuote(quote);
        quote.setDecision(decision);
    }
}
