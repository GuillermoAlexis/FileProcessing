package com.app.FileProcessing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.FileProcessing.dto.FileDTO;
import com.app.FileProcessing.dto.ValidationDetailDTO;
import com.app.FileProcessing.service.impl.FileServiceImpl;
import com.app.FileProcessing.service.impl.ValidationServiceImpl;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileServiceImpl fileService;

	@Autowired
	private ValidationServiceImpl validationService;

	@PostMapping("/process")
	public ResponseEntity<FileDTO> processFile(@RequestParam("file") MultipartFile file) {
		FileDTO fileDTO = fileService.processFile(file);
		return new ResponseEntity<>(fileDTO, HttpStatus.CREATED);
	}

	@GetMapping("/status/{fileId}")
	public ResponseEntity<FileDTO> getFileStatus(@PathVariable Long fileId) {
		FileDTO fileDTO = fileService.getFileStatus(fileId);
		return new ResponseEntity<>(fileDTO, HttpStatus.OK);
	}

	@GetMapping("/validation-details/{fileId}")
	public ResponseEntity<List<ValidationDetailDTO>> getValidationDetails(@PathVariable Long fileId,
	        @RequestParam(required = false) String errorCode) {
	    List<ValidationDetailDTO> details = validationService.getValidationDetail(fileId, errorCode);
	    return new ResponseEntity<>(details, HttpStatus.OK);
	}

}
