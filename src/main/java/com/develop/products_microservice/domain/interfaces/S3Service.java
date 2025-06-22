package com.develop.products_microservice.domain.interfaces;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;


    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${aws.s3.bucket}")
    private String bucketName;


    public String uploadFile(MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String uuidFileName = UUID.randomUUID().toString() + "." + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, uuidFileName, file.getInputStream(), metadata));

        return "https://" + bucketName + ".s3.amazonaws.com/" + uuidFileName;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
