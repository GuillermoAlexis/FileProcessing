package com.app.FileProcessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationDetailDTO {
	private Long id;
	private FileDTO file;
	private Integer lineNumber;
	private String fieldName;
	private Integer errorCode;
	private String errorMessage;
}
