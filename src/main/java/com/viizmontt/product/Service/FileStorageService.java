package com.viizmontt.product.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private static final String UPLOAD_DIR = "public/images/";

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return fileName;
        } catch (IOException ex) {
            logger.error("Error al guardar el archivo: {}", ex.getMessage());
            return null;
        }
    }

    public void deleteFile(String fileName) {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            logger.error("Error al eliminar el archivo: {}", ex.getMessage());
        }
    }
}
