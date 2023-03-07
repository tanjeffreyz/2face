package com.example.demo.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")    // Devops best practice, don't hardcode
public class ImageService {
    final static String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=tanjeffreyz;AccountKey=BBXGU4w9bijlmpb4vMiAnUv5fNhGcERDbKK5ZD3vbdNAy5YN4koBgR23yrXpFUXN5gQ6pRnTvr/M+AStZUr6sw==;EndpointSuffix=core.windows.net";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    private void saveToCloud(byte[] image, String imageName) {
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(CONNECTION_STRING).buildClient();

        // Create a unique name for the container
        String containerName = "images";

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName);
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }

    @PostMapping("/images")
    public ResponseEntity<Object> uploadImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        saveImageToFile(decode, imageName);
        saveToCloud(decode, imageName);
        CustomVision.uploadImage(CustomVision.tags.get("Jeffrey"), decode);
        return new ResponseEntity<>("Successfully uploaded image", HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException { 
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        return CustomVision.validate(decode);
    }
}
