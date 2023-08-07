package com.discphy.manager.file.excel.view;

import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;
import static org.apache.poi.xssf.usermodel.XSSFWorkbookType.XLSX;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;

@Builder
public class ExcelView {

	private final String fileName;
	private final byte[] metaData;

	public ResponseEntity<byte[]> response() {
		return ok().headers(getHeaders()).body(getMetaData());
	}

	public String getFileName() {
		return hasText(fileName) ? fileName : randomUUID().toString();
	}

	public byte[] getMetaData() {
		if (isNull(metaData)) throw new IllegalArgumentException("Excel metadata is required");
		return metaData;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", getFileName() + "." + XLSX.getExtension());

		return headers;
	}
}
