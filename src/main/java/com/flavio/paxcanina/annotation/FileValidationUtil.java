package com.flavio.paxcanina.annotation;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidationUtil {

    public static boolean isValidFileType(MultipartFile file, List<String> acceptedTypes, ConstraintValidatorContext context) {
        String contentType = file.getContentType();

        if (contentType == null || !acceptedTypes.contains(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Type de fichier non autorisé : '" + contentType +
                            "'. Types acceptés : " + String.join(", ", acceptedTypes)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

    public static boolean isValidFileSize(MultipartFile file, long maxSize, ConstraintValidatorContext context) {
        if (maxSize > 0 && file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Fichier trop volumineux : " + file.getSize() + " octets. Limite : " + maxSize + " octets"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
