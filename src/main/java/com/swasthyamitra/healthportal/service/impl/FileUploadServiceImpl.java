package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {


    @Override
    public String storeFile(MultipartFile file) {
        try {
            // Determine upload directory based on OS
            String os = System.getProperty("os.name").toLowerCase();
            String uploadDirPath = os.contains("win")
                    ? new File("custom_assets").getAbsolutePath()
                    : "/var/www/html/custom_assets";

            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists() && uploadDir.mkdirs()) {
                log.info("Created upload directory at {}", uploadDirPath);
            }

            // Break down original filename
            String originalName = file.getOriginalFilename();
            String extension = "";
            String baseName = "file";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf('.'));
                baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            }

            // Generate a unique timestamp + UUID suffix
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("ddMMMyyyy_HHmmss"));  // thread-safe :contentReference[oaicite:1]{index=1}
            String uuidSuffix = UUID.randomUUID()
                    .toString().replace("-", "").substring(0, 8);  // 8 chars from UUID :contentReference[oaicite:2]{index=2}

            String newFileName = String.format("%s_%s_%s%s", baseName, timestamp, uuidSuffix, extension);

            // Save the file
            Path targetPath = Paths.get(uploadDirPath, newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Uploaded '{}' to {}", newFileName, targetPath);

            // Return public URL
            String baseUrl = os.contains("win") ? "http://localhost:8080" : "https://swasthmitra.in";
            return baseUrl + "/custom_assets/" + newFileName;

        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new InvalidInputException("File upload failed: " + e.getMessage());
        }
    }

}
