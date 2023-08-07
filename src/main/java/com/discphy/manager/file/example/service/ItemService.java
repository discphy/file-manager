package com.discphy.manager.file.example.service;

import com.discphy.manager.file.dto.UploadFile;
import com.discphy.manager.file.example.dto.ItemDto;
import com.discphy.manager.file.excel.factory.ExcelManagerFactory;
import com.discphy.manager.file.excel.view.ExcelView;
import com.discphy.manager.file.factory.FileManagerFactory;
import com.discphy.manager.file.view.FileView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

	private final FileManagerFactory fileManagerFactory;
	private final ExcelManagerFactory excelManagerFactory;

	public ExcelView excelDownload() {
		return ExcelView.builder()
				.fileName("item")
				.metaData(excelManagerFactory.write(getList(), ItemDto.class))
				.build();
	}

	public FileView fileDownload(String fileName) {
		return FileView.builder()
				.fileName(fileName)
				.resource(fileManagerFactory.getResource(fileName))
				.build();
	}

	public List<ItemDto> excelUpload(MultipartFile file) {
		List<ItemDto> list = excelManagerFactory.read(file, ItemDto.class);
		log.info("list : {}", list.toString());

		return list;
	}

	public List<UploadFile> fileUpload(List<MultipartFile> files) {
		List<UploadFile> list = fileManagerFactory.saveFiles(files);
		log.info("list : {}", list.toString());

		return list;
	}

	private List<ItemDto> getList() {
		return List.of(
				new ItemDto(1L, "itemA", 1000, 10),
				new ItemDto(2L, "itemB", 2000, 10),
				new ItemDto(3L, "itemC", 3000, 10),
				new ItemDto(4L, "itemD", 4000, 10),
				new ItemDto(5L, "itemE", 5000, 10),
				new ItemDto(6L, "itemF", 6000, 10),
				new ItemDto(7L, "itemG", 7000, 10)
		);
	}
}
