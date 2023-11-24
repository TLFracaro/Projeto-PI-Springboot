package com.projeto_pi.enums;

import lombok.Getter;

@Getter
public enum Extension {

    JPG(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0}),
    JPEG(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0}),
    GIF(new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38}),
    WEBP(new byte[]{(byte) 0x52, (byte) 0x49, (byte) 0x46, (byte) 0x46}),
    PNG(new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47}),
    ICO(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00}),
    SVG(new byte[]{(byte) 0x3C, (byte) 0x3F, (byte) 0x78, (byte) 0x6D});

    private final byte[] magicNumber;

    Extension(byte[] magicNumber) {
        this.magicNumber = magicNumber;
    }
}