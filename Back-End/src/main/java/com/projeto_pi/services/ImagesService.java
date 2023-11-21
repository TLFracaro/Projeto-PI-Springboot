package com.projeto_pi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ImagesService {
    public synchronized String checkIfImageExist(MultipartFile image) {
        try {

            var hash = processImageHash(image);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.readTree(new File("src/main/resources/base64Img.json"));

            var fields = node.fields();

            while (fields.hasNext()) {

                var entry = fields.next();
                String value = entry.getValue().asText();

                if (value.equals(hash.get())) {
                    return entry.getKey();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String uploadImage(MultipartFile image) {
        try {

            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String filename = image.getName() + UUID.randomUUID() + extension;

            var task = processImageReference(image, filename);

            FileOutputStream output = new FileOutputStream(new File("src/main/resources/static/images/", filename));

            output.write(image.getBytes());
            output.close();

            task.get();

            return "http://localhost:8080/images/" + filename;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    protected CompletableFuture<Void> processImageReference(MultipartFile image, String filename) {
        try {
            var task = processImageHash(image);

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            ObjectNode node = mapper.createObjectNode();
            node.put(filename, task.get());

            File file = new File("src/main/resources/base64Img.json");

            JsonNode json = mapper.readTree(file);

            ((ObjectNode) json).setAll(node);

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(file, json);

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<String> processImageHash(MultipartFile image) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            DigestInputStream dis = new DigestInputStream(image.getInputStream(), md);

            while (dis.read() != -1) ;

            byte[] hashBytes = md.digest();

            dis.close();

            StringBuilder builder = new StringBuilder();

            for (byte b : hashBytes) {
                builder.append(String.format("%02x", b));
            }

            return CompletableFuture.completedFuture(builder.toString());
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public synchronized Boolean deleteImage(String urlImageName) {
        try {

            if (urlImageName == null || urlImageName.isEmpty()) {
                return false;
            }

            File file = new File(
                    "src/main/resources/static/images/" + urlImageName
                            .substring(urlImageName.lastIndexOf("/") + 1));

            return file.exists() && file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Boolean deleteJsonReference(String urlImageName) {
        try {

            if (urlImageName == null || urlImageName.isEmpty()) {
                return false;
            }

            urlImageName = urlImageName
                    .substring(urlImageName.lastIndexOf("/") + 1);

            ObjectMapper mapper = new ObjectMapper();

            File file = new File("src/main/resources/base64Img.json");

            ObjectNode node = (ObjectNode) mapper.readTree(file);

            if (node.has(urlImageName)) {
                node.remove(urlImageName);
                mapper.writeValue(file, node);
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
