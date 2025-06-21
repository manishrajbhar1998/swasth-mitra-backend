package com.swasthyamitra.healthportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
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
            String fileUrl = baseUrl + "/custom_assets/" + newFileName;

            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

}