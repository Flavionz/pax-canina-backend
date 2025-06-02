package com.flavio.paxcanina.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ArrayFileValidator implements ConstraintValidator<ValidFile, MultipartFile[]> {

    private List<String> acceptedTypes;
    private long maxSize;

    private final FileConfig fileConfig;

    @Autowired
    public ArrayFileValidator(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        String[] customTypes = constraintAnnotation.acceptedTypes();
        this.acceptedTypes = customTypes.length > 0 ?
                Arrays.asList(customTypes) :
                Arrays.asList(fileConfig.getDefaultAcceptedTypes());
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        if (files == null || files.length == 0) {
            return true; // Nessun file = validazione passata
        }

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                boolean valid = FileValidationUtil.isValidFileType(file, acceptedTypes, context)
                        && FileValidationUtil.isValidFileSize(file, maxSize, context);

                if (!valid) return false; // Basta un file non valido per fallire
            }
        }

        return true;
    }
}
