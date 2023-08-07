package com.discphy.manager.file.excel.utils;

import com.discphy.manager.file.excel.annotation.ExcelColumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Collections.addAll;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toCollection;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public class ExcelUtils {

	public static final Class<ExcelColumn> EXCEL_COLUMN = ExcelColumn.class;
	public static final int HEADER_INDEX = 0, BODY_START_INDEX = 1;

	private static final ObjectMapper mapper = new ObjectMapper();

	public static <T> T mapToClass(Map<String, Object> map, Class<T> type) {
		return mapper.convertValue(map, type);
    }

	public static Map<String, Object> rowToMap(Row row) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            map.put(getColumnName(cell), getCellValue(cell));
        }

        return map;
    }

	public static byte[] workbookToByteArray(Workbook workbook) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			workbook.write(stream);
			return stream.toByteArray();
		} catch (IOException e) {
			log.error("workbookToByteArray error", e);
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> void setHeader(Sheet sheet, Class<T> type) {
		Row row = createHeader(sheet);
		LinkedList<String> headerNames = getLinkedHeaderNames(type);
		CellStyle headerStyle = getDefaultHeaderStyle(sheet.getWorkbook());

		for (int a = 0; a < headerNames.size(); a++) {
			Cell cell = row.createCell(a);
			cell.setCellStyle(headerStyle); // 헤더 셀에 스타일 적용
			cell.setCellValue(headerNames.get(a));
		}
	}

	public static <T> void setBody(Sheet sheet, List<T> list, Class<T> type) {
		LinkedList<Field> fields = getLinkedFields(type);
		int rowNumber = BODY_START_INDEX;

		for (T item : list) {
			setCell(createRow(sheet, rowNumber++), item, fields);
		}
	}

	private static <T> void setCell(Row row, T item, LinkedList<Field> fields) {
		for (int a = 0; a < fields.size(); a++) {
			setCellValue(row.createCell(a), getFieldValue(item, fields.get(a)));
		}
	}

	private static Row createHeader(Sheet sheet) {
		return createRow(sheet, HEADER_INDEX);
	}

    private static Row createRow(Sheet sheet, int rowNumber) {
		return sheet.createRow(rowNumber);
	}

    private static CellStyle getDefaultHeaderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();

		Font font = workbook.createFont();
		font.setBold(true); // bold 스타일 설정

		style.setFont(font);
		style.setFillForegroundColor(GREY_25_PERCENT.getIndex()); // 헤더 색상 설정
		style.setFillPattern(SOLID_FOREGROUND); // 색상 패턴 설정

		return style;
	}
	
    private static String getColumnName(Cell cell) {
        return cell.getSheet().getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
    }

	private static <T> Object getFieldValue(T item, Field field) {
		try {
			field.setAccessible(true);
			return field.get(item);
		} catch (IllegalAccessException e) {
			log.error("getFieldValue error", e);
			throw new IllegalStateException("Failed get field value", e);
		}
	}

	private static <T> LinkedList<Field> getLinkedFields(Class<T> type) {
		LinkedList<Field> fields = getFields(type).stream()
				.filter(ExcelUtils::hasAnnotation)
				.sorted(comparingInt(ExcelUtils::getHeaderOrder))
				.collect(toCollection(LinkedList::new));

		if (isEmpty(fields)) throw new IllegalArgumentException("Excel fields empty");

		return fields;
	}

	private static <T> LinkedList<String> getLinkedHeaderNames(Class<T> type) {
		LinkedList<String> headers = getLinkedFields(type).stream()
				.map(ExcelUtils::getHeaderName)
				.collect(toCollection(LinkedList::new));

		if (isEmpty(headers)) throw new IllegalArgumentException("Excel headers empty");

		return headers;
	}

	private static <T> List<Field> getFields(Class<T> type) {
		List<Field> fields = new ArrayList<>();

		if (nonNull(type.getSuperclass())) {
			addAll(fields, type.getSuperclass().getDeclaredFields());
		}

		addAll(fields, type.getDeclaredFields());

		return fields;
	}

	private static void setCellValue(Cell cell, Object value) {
		if (value instanceof Number numberValue) {
			cell.setCellValue(numberValue.doubleValue());
			return;
		}

		cell.setCellValue(nonNull(value) ? value.toString() : "");
	}

    private static Object getCellValue(Cell cell) {
		return switch (cell.getCellType()) {
			case STRING -> cell.getStringCellValue();
			case NUMERIC -> cell.getNumericCellValue();
			case BOOLEAN -> cell.getBooleanCellValue();
			case FORMULA -> cell.getCellFormula();
			default -> "";
		};
    }

	private static boolean hasAnnotation(Field field) {
		return field.isAnnotationPresent(EXCEL_COLUMN);
	}

	private static int getHeaderOrder(Field field) {
		return field.getAnnotation(EXCEL_COLUMN).headerOrder();
	}

	private static String getHeaderName(Field field) {
		return field.getAnnotation(EXCEL_COLUMN).headerName();
	}
}
