package team.proximity.helpandsupport.FAQ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;
import team.proximity.helpandsupport.FAQGROUP.FAQGroupRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FAQServiceImplTest {

    @Mock
    private FAQRepository faqRepository;

    @Mock
    private FAQGroupRepository faqGroupRepository;

    @Mock
    private FAQMapper faqMapper;

    @InjectMocks
    private FAQServiceImpl faqService;

    private FAQ faq;
    private FAQGroup faqGroup;
    private FAQRequest faqRequest;
    private FAQResponse faqResponse;

    @BeforeEach
    void setUp() {
        faqGroup = new FAQGroup();
        faqGroup.setId(1L);
        faqGroup.setName("general");

        faq = new FAQ();
        faq.setId(1L);
        faq.setQuestion("Test Question");
        faq.setAnswer("Test Answer");
        faq.setGroup(faqGroup);

        faqRequest = new FAQRequest("Test Question", "Test Answer", 1L);
        faqResponse = new FAQResponse(1L, "Test Question", "Test Answer", "general", 1L);
    }

    @Test
    void createFAQ_Success() {

        when(faqGroupRepository.findById(1L)).thenReturn(Optional.of(faqGroup));
        when(faqRepository.findByQuestionAndGroup(anyString(), any(FAQGroup.class)))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> faqService.createFAQ(faqRequest));

        verify(faqRepository).save(any(FAQ.class));
    }

    @Test
    void createFAQ_DuplicateQuestion() {

        when(faqGroupRepository.findById(1L)).thenReturn(Optional.of(faqGroup));
        when(faqRepository.findByQuestionAndGroup(anyString(), any(FAQGroup.class)))
                .thenReturn(Optional.of(faq));

        assertThrows(IllegalArgumentException.class, () -> faqService.createFAQ(faqRequest));
    }

    @Test
    void getFAQById_Success() {

        when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
        when(faqMapper.mapToResponse(faq)).thenReturn(faqResponse);

        FAQResponse result = faqService.getFAQById(1L);

        assertNotNull(result);
        assertEquals(faqResponse.id(), result.id());
        assertEquals(faqResponse.question(), result.question());
    }


    @Test
    void updateFAQ_Success() {

        when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
        when(faqGroupRepository.findById(1L)).thenReturn(Optional.of(faqGroup));


        assertDoesNotThrow(() -> faqService.updateFAQ(1L, faqRequest));


        verify(faqRepository).save(any(FAQ.class));
    }

    @Test
    void deleteFAQ_Success() {

        faqService.deleteFAQ(1L);


        verify(faqRepository).deleteById(1L);
    }
}
