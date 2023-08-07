package com.discphy.manager.file.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static java.util.UUID.randomUUID;

@Slf4j
public class FileUtils {

	public static UrlResource getResource(String path) {
        try {
            return new UrlResource("file:" + path);
        } catch (MalformedURLException e) {
            log.error("File resource error", e);
            throw new IllegalStateException(e);
        }
    }

    public static boolean hasFile(MultipartFile file) {
        return !file.isEmpty();
    }

    public static void validationFile(MultipartFile file) {
        if (!hasFile(file)) throw new IllegalArgumentException("File is empty");
    }

    public static void transfer(MultipartFile file, String path) {
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            log.error("File transfer error", e);
            throw new IllegalStateException(e);
        }
    }

    public static String createFileName(String originalFilename) {
        return randomUUID() + "." + extractExtension(originalFilename);
    }

    private static String extractExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}
