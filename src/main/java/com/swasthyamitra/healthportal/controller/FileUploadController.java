package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileUploadController {

    // Local file system path on the server
    private static final String UPLOAD_DIR = "/var/www/html/custom_assets/";

    // Public base URL for accessing files
    private static final String BASE_URL = "https://swasthmitra.in/custom_assets/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        try {
            // Ensure directory exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists() && dir.mkdirs()) {
                log.info("Created upload directory at {}", UPLOAD_DIR);
            }

            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File '{}' uploaded successfully to {}", fileName, filePath);

            // Build and return the public URL
            String fileUrl = BASE_URL + fileName;
            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            log.error("Failed to upload file '{}': {}", fileName, e.getMessage(), e);
            throw new InvalidInputException("File upload failed: " + e.getMessage());
        }
    }
}

