package com.projeto_pi.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<ValidateBase64, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(value);

            BufferedImage img = ImageIO.read(new ByteArrayInputStream(decodedBytes));

            if (img == null) {
                return false;
            }

            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}