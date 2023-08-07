package com.discphy.manager.file.factory;

import com.discphy.manager.file.dto.UploadFile;
import com.discphy.manager.file.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.discphy.manager.file.utils.FileUtils.*;

@Slf4j
@Component
public class FileManagerFactory {

	@Value("${file.dir}")
    private String fileDir;

    public UrlResource getResource(String fileName) {
        return FileUtils.getResource(getFullPath(fileName));
    }

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> saveFiles(List<MultipartFile> files) {
        List<UploadFile> uploadFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadFiles.add(saveFile(file));
        }

        return uploadFiles;
    }

    public UploadFile saveFile(MultipartFile file)  {
        validationFile(file);

        String originalFilename = file.getOriginalFilename();
        String saveFileName = createFileName(originalFilename);

        transfer(file, getFullPath(saveFileName));

        return UploadFile.builder()
                .uploadFileName(originalFilename)
                .saveFileName(saveFileName)
                .build();
    }
}
