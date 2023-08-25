package com.app.FileProcessing.service;

import org.springframework.web.multipart.MultipartFile;

import com.app.FileProcessing.dto.FileDTO;

public interface FileService {

	/*
	 * Solicitud de procesamiento de archivo: Este es el principal endpoint que
	 * acepta un archivo como parámetro, valida su formato y genera una solicitud de
	 * procesamiento. Incluye validaciones relacionadas con el nombre del archivo,
	 * tamaño y contenido interno.
	 */
	FileDTO processFile(MultipartFile file);

	/*
	 * Consulta de estado del archivo: Este endpoint permite consultar el estado de
	 * un archivo específico. Se proporciona el ID del archivo en la solicitud y se
	 * devuelve información relevante sobre el estado del archivo y el conteo de
	 * registros con errores y sin errores.
	 */

	FileDTO getFileStatus(Long fileId);

}
