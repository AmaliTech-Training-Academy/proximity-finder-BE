package team.proximity.helpandsupport.FAQ;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;
import team.proximity.helpandsupport.FAQGROUP.FAQGroupRepository;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;
    private final FAQGroupRepository faqGroupRepository;
    private final FAQMapper faqMapper;

    public FAQServiceImpl(FAQRepository faqRepository, FAQGroupRepository faqGroupRepository, FAQMapper faqMapper) {
        this.faqRepository = faqRepository;
        this.faqGroupRepository = faqGroupRepository;
        this.faqMapper = faqMapper;
    }

    public void createFAQ(FAQRequest faqRequest) {

        FAQGroup group = faqGroupRepository.findById(faqRequest.groupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + faqRequest.groupId()));

        faqRepository.findByQuestionAndGroup(faqRequest.question(), group)
                .ifPresent(faq -> { throw new IllegalArgumentException("An FAQ with the same question already exists in this group."); });

        FAQ faq = new FAQ();
        faq.setQuestion(faqRequest.question());
        faq.setAnswer(faqRequest.answer());
        faq.setGroup(group);

        faqRepository.save(faq);

    }

    public void updateFAQ(Long id, FAQRequest faqRequest) {
        FAQ existingFAQ = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found with ID: " + id));

        FAQGroup group = faqGroupRepository.findById(faqRequest.groupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + faqRequest.groupId()));

        existingFAQ.setQuestion(faqRequest.question());
        existingFAQ.setAnswer(faqRequest.answer());
        existingFAQ.setGroup(group);

       faqRepository.save(existingFAQ);

    }

    public List<FAQResponse> getFAQsForType(String type) {
        List<String> groupNames = checkAndValidateInput(type);

        return getFAQsForGroups(groupNames);
    }

    private static List<String> checkAndValidateInput(String type) {
        List<String> groupNames;

        if ("provider".equalsIgnoreCase(type)) {
            groupNames = Arrays.asList("general", "provider");
        } else if ("seeker".equalsIgnoreCase(type)) {
            groupNames = Arrays.asList("general", "seeker");
        } else {
            groupNames = List.of("general");
        }
        return groupNames;
    }

    private List<FAQResponse> getFAQsForGroups(List<String> groupNames) {
        Specification<FAQ> spec = FAQSpecifications.belongsToGroupNames(groupNames);
        return faqRepository.findAll(spec)
                .stream()
                .map(faqMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<FAQResponse> getAllFAQs() {
        return faqRepository.findAll().stream()
                .map(faqMapper::mapToResponse)
                .toList();
    }

    public List<FAQResponse> getFAQsByGroup(Long groupId) {
        return faqRepository.findByGroupId(groupId).stream()
                .map(faqMapper::mapToResponse)
                .toList();
    }

    public FAQResponse getFAQById(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found with ID: " + id));
        return faqMapper.mapToResponse(faq);
    }

    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }
}

