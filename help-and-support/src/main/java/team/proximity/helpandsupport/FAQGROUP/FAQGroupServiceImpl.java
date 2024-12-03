package team.proximity.helpandsupport.FAQGROUP;
import org.springframework.stereotype.Service;
import team.proximity.helpandsupport.exception.ResourceAlreadyExistException;

import java.util.List;

@Service
public class FAQGroupServiceImpl implements FAQGroupService {

    private final FAQGroupRepository faqGroupRepository;
    private final FAQGroupMapper faqGroupMapper;

    public FAQGroupServiceImpl(FAQGroupRepository faqGroupRepository, FAQGroupMapper faqGroupMapper) {
        this.faqGroupRepository = faqGroupRepository;
        this.faqGroupMapper = faqGroupMapper;
    }

    public void createGroup(FAQGroupRequest request) {
        boolean exists = faqGroupRepository.findByName(request.name()).isPresent();
        if (exists) {
            throw new ResourceAlreadyExistException("FAQ Group with name '" + request.name() + "' already exists");
        }
        FAQGroup group = new FAQGroup();
        group.setName(request.name());

        faqGroupRepository.save(group);
    }

    public List<FAQGroupResponse> getAllGroups() {
        return faqGroupRepository.findAll().stream()
                .map(faqGroupMapper::mapToResponse)
                .toList();
    }

    public FAQGroupResponse getGroupById(Long id) {
        FAQGroup group = faqGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(" FAQ Group not found with ID: " + id));
        return faqGroupMapper.mapToResponse(group);
    }

}

