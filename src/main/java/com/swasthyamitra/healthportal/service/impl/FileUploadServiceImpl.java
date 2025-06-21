package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {


    @Override
    public String storeFile(MultipartFile file) {

        try {
            // Detect OS to choose correct upload path
            String os = System.getProperty("os.name").toLowerCase();
            String uploadDirPath;

            if (os.contains("win")) {
                // For Windows - relative to project folder
                uploadDirPath = new File("custom_assets").getAbsolutePath();
            } else {
                // For Linux - absolute path (served by Nginx)
                uploadDirPath = "/var/www/html/custom_assets";
            }

            // Ensure directory exists
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists() && uploadDir.mkdirs()) {
                log.info("Created upload directory at {}", uploadDirPath);
            }

            // Extract original extension
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(new Date());
            String newFileName = timestamp + extension;

            // Save file to target path
            Path filePath = Paths.get(uploadDirPath, newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File '{}' uploaded successfully to {}", newFileName, filePath);

            // Build public URL
            String baseUrl = os.contains("win") ? "http://localhost:8080" : "https://swasthmitra.in";
            return baseUrl + "/custom_assets/" + newFileName;


        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new InvalidInputException(e.getMessage());
        }
    }
}
