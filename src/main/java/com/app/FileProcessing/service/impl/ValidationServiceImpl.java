package com.app.FileProcessing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.dto.ValidationDetailDTO;
import com.app.FileProcessing.mapper.ValidationDetailMapper;
import com.app.FileProcessing.model.ValidationDetail;
import com.app.FileProcessing.repository.ValidationDetailRepository;
import com.app.FileProcessing.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Autowired
	private ValidationDetailRepository validationDetailRepository;

	@Override
	public List<ValidationDetailDTO> getValidationDetail(Long fileId, String errorCode) {
		Integer errorCodeInt;
		try {
			errorCodeInt = Integer.parseInt(errorCode);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Código de error inválido");
		}

		List<ValidationDetail> validationDetail = validationDetailRepository.findByFile_IdAndErrorCode(fileId,
				errorCodeInt);

		if (validationDetail == null) {

			throw new IllegalArgumentException(
					"No se encontró detalle de validación para el fileId y errorCode proporcionados");

		}

		return ValidationDetailMapper.INSTANCE.toDTOList(validationDetail);

	}
}
