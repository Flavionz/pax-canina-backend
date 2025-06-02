package com.flavio.paxcanina.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Service
public class FileService {

    @Value("${public.upload.folder}")
    private String publicUploadFolder;

    @Value("${private.upload.folder}")
    private String privateUploadFolder;

    /**
     * Metodo principale per l'upload, supporta sia MultipartFile che InputStream
     */
    public void uploadToLocalFileSystem(MultipartFile file, String fileName, boolean isPublic) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            uploadToLocalFileSystem(inputStream, fileName, isPublic);
        }
    }

    /**
     * Sovraccarico per usare direttamente un InputStream
     */
    public void uploadToLocalFileSystem(InputStream inputStream, String fileName, boolean isPublic) throws IOException {
        Path storageDirectory = Paths.get(isPublic ? publicUploadFolder : privateUploadFolder);

        if (!Files.exists(storageDirectory)) {
            try {
                Files.createDirectories(storageDirectory);
            } catch (IOException e) {
                throw new IOException("Impossible de créer le répertoire de stockage", e);
            }
        }

        Path destination = storageDirectory.resolve(fileName);
        Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Recupera l'immagine, specificando se è pubblica o privata
     */
    public byte[] getImageByName(String fileName, boolean isPublic) throws IOException {
        Path filePath = Paths.get((isPublic ? publicUploadFolder : privateUploadFolder), fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Fichier non trouvé: " + fileName);
        }
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    /**
     * Metodo ausiliario per costruire l'URL pubblico dell'immagine
     */
    public String buildPublicUrl(String fileName) {
        return "/images/" + fileName;
    }
}
