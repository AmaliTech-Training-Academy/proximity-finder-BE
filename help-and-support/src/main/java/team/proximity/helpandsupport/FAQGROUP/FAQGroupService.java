package team.proximity.helpandsupport.FAQGROUP;

import java.util.List;

public interface FAQGroupService {
    void createGroup(FAQGroupRequest request);

    List<FAQGroupResponse> getAllGroups();

    FAQGroupResponse getGroupById(Long id);
}
