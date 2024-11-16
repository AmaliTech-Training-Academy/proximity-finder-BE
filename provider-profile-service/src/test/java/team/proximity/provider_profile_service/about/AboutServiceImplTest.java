package team.proximity.provider_profile_service.about;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.provider_profile_service.upload.FileUploadService;
import team.proximity.provider_profile_service.validations.AboutValidator;
import team.proximity.provider_profile_service.validations.FileValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AboutServiceImplTest {
    @Mock
    private FileUploadService fileStorageService;

    @Mock
    private AboutRepository aboutRepository;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private AboutValidator aboutValidator;

    @InjectMocks
    private AboutServiceImpl aboutService;

    @Test
    void testCreateOneAboutSuccessfulCreation() throws IOException {

        MultipartFile identityFile = mock(MultipartFile.class);
        MultipartFile certificateFile = mock(MultipartFile.class);
        AboutRequest request = new AboutRequest(
                LocalDate.of(2023, 1, 1),
                Set.of("https://example.com"),
                10,
                identityFile,
                certificateFile,
                "Business summary"
        );

        when(fileStorageService.uploadFile(identityFile)).thenReturn("identityUrl");
        when(fileStorageService.uploadFile(certificateFile)).thenReturn("certificateUrl");


        aboutService.createOneAbout(request);
        verify(aboutValidator).validate(request);
        verify(fileValidator).validate(identityFile);
        verify(fileValidator).validate(certificateFile);

    }

    @Test
    void testCreateOneAbout_ValidationFails() {

        AboutRequest request = new AboutRequest(
                LocalDate.of(2023, 1, 1),
                Set.of("https://example.com"),
                10,
                mock(MultipartFile.class),
                mock(MultipartFile.class),
                "Business summary"
        );
        doThrow(new IllegalArgumentException("Invalid request")).when(aboutValidator).validate(request);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            aboutService.createOneAbout(request);
        });
        assertEquals("Invalid request", exception.getMessage());

        verifyNoInteractions(fileStorageService, aboutRepository);
    }

    @Test
    void testCreateOneAboutFileUploadFails() throws IOException {

        MultipartFile identityFile = mock(MultipartFile.class);
        MultipartFile certificateFile = mock(MultipartFile.class);
        AboutRequest request = new AboutRequest(
                LocalDate.of(2023, 1, 1),
                Set.of("https://example.com"),
                10,
                identityFile,
                certificateFile,
                "Business summary"
        );

        doThrow(new IOException("File upload failed")).when(fileStorageService).uploadFile(identityFile);


        IOException exception = assertThrows(IOException.class, () -> {
            aboutService.createOneAbout(request);
        });
        assertEquals("File upload failed", exception.getMessage());

        verify(fileValidator).validate(identityFile);
        verify(fileStorageService).uploadFile(identityFile);

    }
}
