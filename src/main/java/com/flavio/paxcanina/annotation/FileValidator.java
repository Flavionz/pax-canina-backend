package com.flavio.paxcanina.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private List<String> acceptedTypes;
    private long maxSize;

    private final FileConfig fileConfig;

    @Autowired
    public FileValidator(FileConfig fileConfig) {
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
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        return FileValidationUtil.isValidFileType(file, acceptedTypes, context)
                && FileValidationUtil.isValidFileSize(file, maxSize, context);
    }
}
