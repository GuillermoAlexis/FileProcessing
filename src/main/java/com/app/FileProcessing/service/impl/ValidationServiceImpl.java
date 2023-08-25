package com.app.FileProcessing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.dto.ValidationDetailDTO;
import com.app.FileProcessing.repository.ValidationDetailRepository;
import com.app.FileProcessing.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Autowired
	private ValidationDetailRepository validationRepository;

	@Override
	public ValidationDetailDTO getValidationDetail(Long fileId, String errorCode) {
		// Obtener los detalles de validación de un archivo en base al ID del archivo y
		// el código de error
		return null; // Modificar según la lógica
	}
}
