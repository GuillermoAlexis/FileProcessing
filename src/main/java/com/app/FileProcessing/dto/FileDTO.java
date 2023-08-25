package com.app.FileProcessing.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
	private Long id;
	private String fileName;
	private String status;
	private Timestamp processedAt;
	private AppUserWithoutRolesDTO user;
}
