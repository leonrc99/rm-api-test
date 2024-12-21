package com.reliquiasdamagia.api_rm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.url}")
    private String serverUrl;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("Arquivo vazio");
                }

                // Cria o diretório, caso não exista
                Path directoryPath = Paths.get(uploadDir);
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                }

                // Define o caminho do arquivo
                String fileName = file.getOriginalFilename();
                Path filePath = directoryPath.resolve(fileName);

                // Salva o arquivo no disco
                Files.write(filePath, file.getBytes());

                // Adiciona a URL do arquivo à lista de resposta
                fileUrls.add(serverUrl + "/files/" + fileName);
            }
            return ResponseEntity.ok(fileUrls);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
