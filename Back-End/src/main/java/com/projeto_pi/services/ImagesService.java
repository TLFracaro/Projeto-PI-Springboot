package com.projeto_pi.services;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projeto_pi.dtos.ImagemDto;

@Service
public class ImagesService {

    public String checkIfImageExist(String base64Img) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.readTree(new File("src/main/resources/base64Img.json"));

            var fields = node.fields();

            while (fields.hasNext()) {

                var entry = fields.next();
                String value = entry.getValue().asText();

                if (value.equals(base64Img.substring(0, 100))) {
                    return entry.getKey();
                }
            }
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadImage(ImagemDto imagem) {
        try {

            if (imagem.getBase64().contains("image_")) {
                return new StringBuilder("http://127.0.0.1:8080/images/" + imagem.getBase64()).toString();
            }

            String filename = new StringBuilder("image_" + UUID.randomUUID() + imagem.getExtension()).toString();

            var task = processImageReference(filename, imagem);

            FileOutputStream output = new FileOutputStream(
                    new File("src/main/resources/static/images/", filename));

            output.write(Base64.getDecoder().decode(imagem.getBase64()));
            output.close();

            task.get();

            return new StringBuilder("http://127.0.0.1:8080/images/" + filename).toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    private CompletableFuture<Void> processImageReference(String filename, ImagemDto imagem) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            JsonNode node = mapper.createObjectNode();
            ((ObjectNode) node).put(filename, imagem.getBase64().substring(0, 100));

            File file = new File("src/main/resources/base64Img.json");

            JsonNode json = mapper.readTree(file);

            ((ObjectNode) json).setAll((ObjectNode) node);

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(file, json);

            return CompletableFuture.completedFuture(null);
        }
        catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public Boolean deleteImage(String urlImageName) {
        try {

            if (urlImageName == null || urlImageName.equals("")) {
                throw new IllegalArgumentException("Não há um tipo válido na URL da imagem");
            }

            boolean condition = false;

            File file = new File(
                    new StringBuilder("src/main/resources/static/images/" + urlImageName
                            .substring(urlImageName.lastIndexOf("/") + 1))
                            .toString());

            if (file.exists()) {
                condition = file.delete();
            }

            return condition;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteJsonReference(String urlImageName) {
        try {

            if (urlImageName == null || urlImageName.equals("")) {
                throw new IllegalArgumentException("Não há um tipo válido na URL da imagem");
            }

            urlImageName = new StringBuilder(urlImageName
                    .substring(urlImageName.lastIndexOf("/") + 1))
                    .toString();

            ObjectMapper mapper = new ObjectMapper();

            File file = new File("src/main/resources/base64Img.json");

            ObjectNode node = (ObjectNode) mapper.readTree(file);

            if (node.has(urlImageName)) {
                node.remove(urlImageName);
                mapper.writeValue(file, node);
            }

            return true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
