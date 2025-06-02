package com.flavio.paxcanina.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { FileValidator.class, ArrayFileValidator.class })
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {

    String message() default "Fichier invalide (type ou taille)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] acceptedTypes() default {};  // es. "image/png", "image/jpeg"

    long maxSize() default -1; // in byte (-1 = illimité)
}
