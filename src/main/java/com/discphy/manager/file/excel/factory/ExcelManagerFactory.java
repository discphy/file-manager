package com.discphy.manager.file.excel.factory;

import com.discphy.manager.file.factory.FileManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.discphy.manager.file.excel.utils.ExcelUtils.*;

@Slf4j
@Component
public class ExcelManagerFactory extends FileManagerFactory {

	public <T> List<T> read(MultipartFile file, Class<T> type)  {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			super.saveFile(file);
			return getList(workbook.getSheetAt(0), type);
		} catch (IOException e) {
			log.error("Rendering read error", e);
			throw new IllegalStateException(e);
		}
	}

	public <T> byte[] write(List<T> list, Class<T> type) {
		try (Workbook workbook = new SXSSFWorkbook()) {
			setList(workbook.createSheet(), list, type);
			return workbookToByteArray(workbook);
		} catch (IOException e) {
			log.error("Rendering write error", e);
			throw new IllegalStateException(e);
		}
	}

	private <T> List<T> getList(Sheet sheet, Class<T> type) {
		List<T> list = new ArrayList<>();

		for (int a = BODY_START_INDEX; a < sheet.getLastRowNum(); a++) {
			list.add(mapToClass(rowToMap(sheet.getRow(a)), type));
		}

		return list;
	}

	private <T> void setList(Sheet sheet, List<T> list, Class<T> type) {
		setHeader(sheet, type);
		setBody(sheet, list, type);
	}
}
