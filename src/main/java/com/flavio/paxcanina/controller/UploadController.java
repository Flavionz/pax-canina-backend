package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import java.io.IOException;
import java.util.List;

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
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "true") boolean isPublic
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier manquant");
        }

        List<String> acceptedTypes = List.of("image/jpeg", "image/png");
        if (!acceptedTypes.contains(file.getContentType())) {
            return ResponseEntity.badRequest().body("Type de fichier non supporté: " + file.getContentType());
        }

        if (file.getSize() > 1048576) {
            return ResponseEntity.badRequest().body("Fichier trop volumineux (max 1MB)");
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            fileService.uploadToLocalFileSystem(file, fileName, isPublic);

            String fileUrl = isPublic
                    ? fileService.buildPublicUrl(fileName)
                    : "Upload réussi (fichier privé non accessible publiquement)";

            return ResponseEntity.ok().body(Map.of("url", fileUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l’upload : " + e.getMessage());
        }
    }
}
