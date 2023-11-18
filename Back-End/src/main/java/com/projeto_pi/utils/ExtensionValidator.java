package com.projeto_pi.utils;

import com.projeto_pi.enums.Extensions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExtensionValidator implements ConstraintValidator<ValidateExtension, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (Extensions extension : Extensions.values()) {
            if (value.equals(extension.getExtension())) {
                return true;
            }
        }
        return false;
    }
    
}
