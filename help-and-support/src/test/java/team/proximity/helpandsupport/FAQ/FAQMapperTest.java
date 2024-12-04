package team.proximity.helpandsupport.FAQ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;

import static org.junit.jupiter.api.Assertions.*;

class FAQMapperTest {

    private FAQMapper faqMapper;
    private FAQ faq;
    private FAQGroup faqGroup;

    @BeforeEach
    void setUp() {
        faqMapper = new FAQMapper();
        faqGroup = new FAQGroup();
        faqGroup.setId(1L);
        faqGroup.setName("General Questions");

        faq = new FAQ();
        faq.setId(1L);
        faq.setQuestion("What is Proximity?");
        faq.setAnswer("Proximity is a platform that helps connect people.");
        faq.setGroup(faqGroup);
    }

    @Test
    @DisplayName("Should correctly map FAQ to FAQResponse")
    void shouldMapFAQToFAQResponse() {
        FAQResponse response = faqMapper.mapToResponse(faq);

        assertNotNull(response, "FAQResponse should not be null");
        assertEquals(faq.getId(), response.id(), "ID should match");
        assertEquals(faq.getQuestion(), response.question(), "Question should match");
        assertEquals(faq.getAnswer(), response.answer(), "Answer should match");
        assertEquals(faq.getGroup().getName(), response.groupName(), "Group name should match");
        assertEquals(faq.getGroup().getId(), response.groupId(), "Group ID should match");
    }

    @Test
    @DisplayName("Should handle FAQ with null group")
    void shouldHandleFAQWithNullGroup() {

        FAQ faqWithNullGroup = new FAQ();
        faqWithNullGroup.setId(2L);
        faqWithNullGroup.setQuestion("Test Question");
        faqWithNullGroup.setAnswer("Test Answer");
        faqWithNullGroup.setGroup(null);


        assertThrows(NullPointerException.class, () -> {
            faqMapper.mapToResponse(faqWithNullGroup);}, "Should throw NullPointerException when group is null");
    }

    @Test
    @DisplayName("Should handle null FAQ")
    void shouldHandleNullFAQ() {

        assertThrows(NullPointerException.class, () -> {
            faqMapper.mapToResponse(null);}, "Should throw NullPointerException when FAQ is null");
    }
}
