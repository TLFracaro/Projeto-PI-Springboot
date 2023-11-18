package com.projeto_pi.enums;

public enum Extensions {

    JPEG(".jpeg"),
    JPG(".jpg"),
    PNG(".png"),
    SVG(".svg"),
    GIF(".gif"),
    WEBP(".webp"),
    ICO(".ico");

    Extensions(String extension) {
        this.extension = extension;
    }

    private String extension;

    public String getExtension() {
        return extension;
    }
}
