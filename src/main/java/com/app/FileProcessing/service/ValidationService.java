package com.app.FileProcessing.service;

import com.app.FileProcessing.dto.ValidationDetailDTO;

public interface ValidationService {

	/*
	 * Detalle de validación de un archivo: Este endpoint permite obtener el detalle
	 * de validación de un archivo en función del ID del archivo y el código de
	 * error. Devuelve información detallada sobre los errores encontrados en los
	 * registros del archivo.
	 */

	ValidationDetailDTO getValidationDetail(Long fileId, String errorCode);

}
