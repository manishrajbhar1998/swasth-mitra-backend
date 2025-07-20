package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.exception.InvalidInputException;
import com.swasthyamitra.healthportal.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

       String url = fileUploadService.storeFile(file);

       return ResponseEntity.ok(url);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource>  downloadImageAsBase64Bytes(@RequestParam String imageUrl) {

            // Download image as raw bytes
            byte[] imageBytes = fileUploadService.downloadImageAsBytes(imageUrl);

            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "abc");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imageBytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

    }

}