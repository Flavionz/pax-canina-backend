package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.annotation.ValidFile;
import com.flavio.paxcanina.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class UploadController {

    private final FileService fileService;

    @Autowired
    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") @ValidFile(acceptedTypes = {"image/jpeg", "image/png"}, maxSize = 1048576) MultipartFile file,
            @RequestParam(defaultValue = "true") boolean isPublic
    ) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            fileService.uploadToLocalFileSystem(file, fileName, isPublic);

            String fileUrl = isPublic
                    ? fileService.buildPublicUrl(fileName)
                    : "Upload réussi (fichier privé non accessible publiquement)";

            return ResponseEntity.ok().body(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l’upload : " + e.getMessage());
        }
    }
}
