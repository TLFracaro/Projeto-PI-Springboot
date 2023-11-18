package com.projeto_pi.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64Validator.class)
public @interface ValidateBase64 {

    String message() default "A imagem inserida não possui um conteúdo válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
