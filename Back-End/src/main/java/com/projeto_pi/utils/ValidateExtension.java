package com.projeto_pi.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExtensionValidator.class)
public @interface ValidateExtension {

    String message() default "A imagem inserida não possui uma extensão válida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
