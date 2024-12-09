package team.proximity.helpandsupport.FAQ;

import org.springframework.stereotype.Component;

@Component
public class FAQMapper {

    public FAQResponse mapToResponse(FAQ faq) {

        return new FAQResponse(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getGroup().getName(),
                faq.getGroup().getId()
        );
    }
}
