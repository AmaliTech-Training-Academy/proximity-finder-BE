package team.proximity.helpandsupport.FAQGROUP;

import org.springframework.stereotype.Component;

@Component
public class FAQGroupMapper {

    public FAQGroupResponse mapToResponse(FAQGroup faqGroup){

        return new FAQGroupResponse(
                faqGroup.getId(),
                faqGroup.getName()
        );

    }
}
