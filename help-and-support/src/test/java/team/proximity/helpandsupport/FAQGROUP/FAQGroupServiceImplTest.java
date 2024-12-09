package team.proximity.helpandsupport.FAQGROUP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.proximity.helpandsupport.exception.ResourceAlreadyExistException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FAQGroupServiceImplTest {

    @Mock
    private FAQGroupRepository faqGroupRepository;

    @Mock
    private FAQGroupMapper faqGroupMapper;

    @InjectMocks
    private FAQGroupServiceImpl faqGroupService;

    private FAQGroup faqGroup;
    private FAQGroupRequest request;
    private FAQGroupResponse response;

    @BeforeEach
    void setUp() {
        faqGroup = new FAQGroup();
        faqGroup.setId(1L);
        faqGroup.setName("test-group");

        request = new FAQGroupRequest("test-group");
        response = new FAQGroupResponse(1L, "test-group");
    }

    @Test
    void createGroup_Success() {

        when(faqGroupRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(faqGroupRepository.save(any(FAQGroup.class))).thenReturn(faqGroup);


        faqGroupService.createGroup(request);


        verify(faqGroupRepository).save(any(FAQGroup.class));
    }

    @Test
    void createGroup_ThrowsWhenGroupExists() {

        when(faqGroupRepository.findByName(request.name())).thenReturn(Optional.of(faqGroup));


        assertThrows(ResourceAlreadyExistException.class, () -> {
            faqGroupService.createGroup(request);
        });

        verify(faqGroupRepository, never()).save(any(FAQGroup.class));
    }

    @Test
    void getAllGroups_Success() {

        List<FAQGroup> groups = List.of(faqGroup);
        when(faqGroupRepository.findAll()).thenReturn(groups);
        when(faqGroupMapper.mapToResponse(faqGroup)).thenReturn(response);


        List<FAQGroupResponse> result = faqGroupService.getAllGroups();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
        verify(faqGroupMapper).mapToResponse(faqGroup);
    }

    @Test
    void getGroupById_Success() {
        when(faqGroupRepository.findById(1L)).thenReturn(Optional.of(faqGroup));
        when(faqGroupMapper.mapToResponse(faqGroup)).thenReturn(response);

        FAQGroupResponse result = faqGroupService.getGroupById(1L);

        assertThat(result).isEqualTo(response);
        verify(faqGroupMapper).mapToResponse(faqGroup);
    }

    @Test
    void getGroupById_ThrowsWhenNotFound() {

        when(faqGroupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            faqGroupService.getGroupById(1L);
        });

        verify(faqGroupMapper, never()).mapToResponse(any());
    }
}
