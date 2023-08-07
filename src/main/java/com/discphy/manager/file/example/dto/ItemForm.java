package com.discphy.manager.file.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    private MultipartFile excelFile;
    private List<MultipartFile> attachFiles;

}
