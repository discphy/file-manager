package com.discphy.manager.file.example.controller;

import com.discphy.manager.file.example.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/excel/download")
	public ResponseEntity<byte[]> excelDownload() {
		return itemService.excelDownload().response();
	}

	@GetMapping("/file/download/{filename}")
	public ResponseEntity<Resource> fileDownload(@PathVariable String filename) {
		return itemService.fileDownload(filename).response();
	}
}
