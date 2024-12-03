package team.proximity.provider_profile_service.about;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.upload.FileUploadService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AboutServiceImplTest {

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private AboutRepository aboutRepository;


    @InjectMocks
    private AboutServiceImpl aboutService;

    @Test
    void createOneAboutShouldCreateNewAboutRecord() throws IOException {

        String username = "testUser";
        MockedStatic<AuthHelper> authHelper = mockStatic(AuthHelper.class);
        authHelper.when(AuthHelper::getAuthenticatedUsername).thenReturn(username);

        AboutRequest aboutRequest = new AboutRequest(

            LocalDate.now(),
            Set.of("link1", "link2"),
            10,
            mock(MultipartFile.class),
            mock(MultipartFile.class),
            "Test summary"
        );

        when(aboutRepository.findByCreatedBy(username)).thenReturn(Optional.empty());
        when(fileUploadService.uploadFile(any())).thenReturn("path/to/file");


        aboutService.createOneAbout(aboutRequest);


        verify(fileUploadService, times(2)).uploadFile(any());

        ArgumentCaptor<About> aboutCaptor = ArgumentCaptor.forClass(About.class);
        verify(aboutRepository).save(aboutCaptor.capture());

        About savedAbout = aboutCaptor.getValue();
        assertEquals(username, savedAbout.getCreatedBy());
        assertEquals(aboutRequest.inceptionDate(), savedAbout.getInceptionDate());
        assertEquals(aboutRequest.socialMediaLinks(), savedAbout.getSocialMediaLinks());
        assertEquals(aboutRequest.numberOfEmployees(), savedAbout.getNumberOfEmployees());
        assertEquals("path/to/file", savedAbout.getBusinessIdentityCard());
        assertEquals("path/to/file", savedAbout.getBusinessCertificate());
        assertEquals(aboutRequest.businessSummary(), savedAbout.getBusinessSummary());

        authHelper.close();
    }

    @Test
    void createOneAboutShouldDeleteExistingAndCreateNewAboutRecord() throws IOException {

        String username = "testUser";
        MockedStatic<AuthHelper> authHelper = mockStatic(AuthHelper.class);
        authHelper.when(AuthHelper::getAuthenticatedUsername).thenReturn(username);

        About existingAbout = About.builder()
            .createdBy(username)
            .build();

        AboutRequest aboutRequest = new AboutRequest(

            LocalDate.now(),
            Set.of("link1", "link2"),
            10,
            mock(MultipartFile.class),
            mock(MultipartFile.class),
            "Test summary"
        );

        when(aboutRepository.findByCreatedBy(username)).thenReturn(Optional.of(existingAbout));
        when(fileUploadService.uploadFile(any())).thenReturn("path/to/file");


        aboutService.createOneAbout(aboutRequest);


        verify(aboutRepository).delete(existingAbout);
        verify(fileUploadService, times(2)).uploadFile(any());

        ArgumentCaptor<About> aboutCaptor = ArgumentCaptor.forClass(About.class);
        verify(aboutRepository).save(aboutCaptor.capture());

        About savedAbout = aboutCaptor.getValue();
        assertEquals(username, savedAbout.getCreatedBy());

        authHelper.close();
    }

    @Test
    void createOneAboutShouldThrowIOExceptionWhenFileUploadFails() throws IOException {

        String username = "testUser";
        MockedStatic<AuthHelper> authHelper = mockStatic(AuthHelper.class);
        authHelper.when(AuthHelper::getAuthenticatedUsername).thenReturn(username);

        AboutRequest aboutRequest = new AboutRequest(

            LocalDate.now(),
            Set.of("link1", "link2"),
            10,
            mock(MultipartFile.class),
            mock(MultipartFile.class),
            "Test summary"
        );

        when(aboutRepository.findByCreatedBy(username)).thenReturn(Optional.empty());
        when(fileUploadService.uploadFile(any())).thenThrow(new IOException("Upload failed"));


        assertThrows(IOException.class, () -> aboutService.createOneAbout(aboutRequest));
        verify(aboutRepository, never()).save(any());

        authHelper.close();
    }
}



