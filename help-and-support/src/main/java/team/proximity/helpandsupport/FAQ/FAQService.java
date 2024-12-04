package team.proximity.helpandsupport.FAQ;

import java.util.List;

public interface FAQService {


     void createFAQ(FAQRequest faqRequest);
     void updateFAQ(Long id, FAQRequest faqRequest);
     List<FAQResponse> getAllFAQs();
     List<FAQResponse> getFAQsByGroup(Long groupId);
     FAQResponse getFAQById(Long id);
     void deleteFAQ(Long id);
     List<FAQResponse> getFAQsForType(String type);

}
