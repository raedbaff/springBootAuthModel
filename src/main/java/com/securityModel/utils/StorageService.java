package com.securityModel.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class StorageService {
    private final Path rootLocation = Paths.get("upload-dir");


    public String store(MultipartFile file) {
        try {
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String original = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));
            String fileName = original + "_" + Integer.toString(new Random().nextInt(1000000000)) + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
            return fileName; // Return the generated file name
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }




    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }


    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
