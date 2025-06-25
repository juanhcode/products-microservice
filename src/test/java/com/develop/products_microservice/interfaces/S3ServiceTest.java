package com.develop.products_microservice.interfaces;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.develop.products_microservice.domain.interfaces.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        // Configurar el valor de la propiedad inyectada manualmente
        s3Service.getClass().getDeclaredFields();
        try {
            var field = S3Service.class.getDeclaredField("bucketName");
            field.setAccessible(true);
            field.set(s3Service, "test-bucket");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting bucketName", e);
        }
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        byte[] content = "archivo de prueba".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test-image.png", "image/png", content
        );

        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        // Act
        String url = s3Service.uploadFile(mockFile);

        // Assert
        assertNotNull(url);
        assertTrue(url.startsWith("https://test-bucket.s3.amazonaws.com/"));
        assertTrue(url.endsWith(".png"));

        verify(amazonS3).putObject(captor.capture());

        PutObjectRequest putObjectRequest = captor.getValue();
        assertEquals("test-bucket", putObjectRequest.getBucketName());
        assertTrue(putObjectRequest.getKey().endsWith(".png"));
        assertNotNull(putObjectRequest.getInputStream());
        assertEquals("image/png", putObjectRequest.getMetadata().getContentType());
        assertEquals(content.length, putObjectRequest.getMetadata().getContentLength());
    }

    @Test
    void testGetExtension() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "photo.jpeg", "image/jpeg", "test".getBytes()
        );

        String url = s3Service.uploadFile(file);
        assertTrue(url.endsWith(".jpeg"));
    }
}
