package com.discphy.manager.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UploadFile {

	private String uploadFileName;
    private String saveFileName;

    @Builder
    public UploadFile(String uploadFileName, String saveFileName) {
        this.uploadFileName = uploadFileName;
        this.saveFileName = saveFileName;
    }
}
