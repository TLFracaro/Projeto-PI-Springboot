package com.projeto_pi.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.projeto_pi.enums.Extension;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<ValidateImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if (file == null) {
            return false;
        }
        try (InputStream inputStream = file.getInputStream()) {

            byte[] signature = new byte[4];

            if (inputStream.read(signature) != -1) {
                for (Extension extension : Extension.values()) {

                    byte[] magicNumber = extension.getMagicNumber();

                    if (signature.length >= magicNumber.length) {
                        boolean matches = true;
                        for (int i = 0; i < magicNumber.length; i++) {
                            if (signature[i] != magicNumber[i]) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }
}