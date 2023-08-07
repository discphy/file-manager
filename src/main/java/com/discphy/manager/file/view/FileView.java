package com.discphy.manager.file.view;

import lombok.Builder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriUtils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;

@Builder
public class FileView {

	private final String fileName;
	private final UrlResource resource;

	public ResponseEntity<Resource> response() {
		return ok().headers(getHeaders()).body(getResource());
	}

	public String getFileName() {
		if (!hasText(fileName)) throw new IllegalArgumentException("File name is required");
		return UriUtils.encode(fileName, UTF_8);
	}

	public UrlResource getResource() {
		if (isNull(resource)) throw new IllegalArgumentException("File resource is required");
		return resource;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", getFileName());

		return headers;
	}
}
