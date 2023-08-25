package com.app.FileProcessing.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.dto.FileDTO;
import com.app.FileProcessing.mapper.AppUserMapper;
import com.app.FileProcessing.mapper.FileMapper;
import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.model.File;
import com.app.FileProcessing.model.ValidationDetail;
import com.app.FileProcessing.repository.FileRepository;
import com.app.FileProcessing.repository.ValidationDetailRepository;
import com.app.FileProcessing.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private ValidationDetailRepository validationDetailRepository;

	@Autowired
	private FileRepository fileRepository;

	public FileDTO processFile(MultipartFile file) {

		// Validar nombre del archivo
		String fileName = file.getOriginalFilename();
		long existingFileCount = fileRepository.countByFileName(fileName);
		if (fileName == null || existingFileCount > 0) {
			throw new IllegalArgumentException("Nombre de archivo inválido o ya procesado.");
		}

		// Validar extensión
		if (!fileName.endsWith(".DAT")) {
			throw new IllegalArgumentException("La extensión del archivo no es válida.");
		}

		// Validar tamaño
		long fileSize = file.getSize();
		if (fileSize < 1024 || fileSize > 2 * 1024 * 1024) {
			throw new IllegalArgumentException("Tamaño de archivo no válido.");
		}

		// Validar formato del nombre del archivo
		String[] parts = fileName.split("_");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Formato de nombre de archivo incorrecto.");
		}

		// Validar fecha
		String datePart = parts[0];
		if (!datePart.matches("\\d{8}")) {
			throw new IllegalArgumentException("La fecha en el nombre de archivo es incorrecta.");
		}

		// Validar entidad
		String entityPart = parts[1];
		if (!entityPart.matches("[a-zA-Z]{5,15}")) {
			throw new IllegalArgumentException("El nombre de la entidad en el archivo es incorrecto.");
		}

		// Validar versión
		String versionPart = parts[2].split("\\.")[0];
		if (!versionPart.matches("\\d{3}")) {
			throw new IllegalArgumentException("La versión en el nombre del archivo es incorrecta.");
		}

		// Convertir el archivo en entidades y guardar en la base de datos
		File fileEntity = new File();
		fileEntity.setFileName(fileName);
		fileEntity.setStatus("recibido");
		fileEntity.setProcessedAt(new Timestamp(System.currentTimeMillis()));

		fileEntity = fileRepository.save(fileEntity);

		// Procesar de manera asíncrona el contenido del archivo
		asyncProcessFileContent(file, fileEntity);

		// Retornar la respuesta
		return FileMapper.INSTANCE.toDTO(fileEntity);
	}

	@Async
	public void asyncProcessFileContent(MultipartFile file, File fileEntity) {
		try {
			String content = new String(file.getBytes(), StandardCharsets.UTF_8);
			String[] lines = content.split("\n");
			List<ValidationDetail> validationDetails = new ArrayList<>();

			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (line.length() >= 2) {
					String type = line.substring(0, 2).trim();
					String entity = line.substring(2, 17).trim();
					String dateStr = line.substring(17, 25).trim();

					if (!type.matches("[0-9]{2}")) {
						addValidationError(validationDetails, fileEntity, i + 1, "Tipo", 300,
								"El campo 'Tipo de Registro' debe ser numérico.");
					} else if ("01".equals(type) && !entity.equals("SERMALUC")
							&& !entity.equals(extractEntityNameFromFile(fileEntity))) {
						addValidationError(validationDetails, fileEntity, i + 1, "Entidad", 2,
								"El campo debe ser igual a “SERMALUC” o “NombreEntidad”.");
					}

					if (!dateStr.matches("[0-9]{8}")) {
						addValidationError(validationDetails, fileEntity, i + 1, "Fecha", 302,
								"El campo 'Fecha' debe ser numérico.");
					} else {
						int year = Integer.parseInt(dateStr.substring(0, 4));
						int month = Integer.parseInt(dateStr.substring(4, 6));
						int day = Integer.parseInt(dateStr.substring(6, 8));
						LocalDate currentDate = LocalDate.now();

						if (year < 1995) {
							addValidationError(validationDetails, fileEntity, i + 1, "Fecha", 11,
									"El año del campo 'Fecha' debe ser mayor o igual a 1995.");
						}

						try {
							LocalDate parsedDate = LocalDate.of(year, month, day);
							if (parsedDate.isAfter(currentDate)) {
								addValidationError(validationDetails, fileEntity, i + 1, "Fecha", 102,
										"La fecha del campo 'Fecha' debe ser menor o igual a la fecha del sistema.");
							}
						} catch (DateTimeException e) {
							addValidationError(validationDetails, fileEntity, i + 1, "Fecha", 100, "Fecha no válida.");
						}
					}
				}
			}

			validationDetailRepository.saveAll(validationDetails);

			if (validationDetails.isEmpty()) {
				fileEntity.setStatus("validado");
			} else {
				fileEntity.setStatus("procesado con errores");
			}
			fileRepository.save(fileEntity);

		} catch (IOException e) {
			fileEntity.setStatus("procesado con errores");
			fileRepository.save(fileEntity);
		}
	}

	private void addValidationError(List<ValidationDetail> validationDetails, File fileEntity, int lineNumber,
			String fieldName, int errorCode, String errorMessage) {
		ValidationDetail detail = new ValidationDetail();
		detail.setFile(fileEntity);
		detail.setLineNumber(lineNumber);
		detail.setFieldName(fieldName);
		detail.setErrorCode(errorCode);
		detail.setErrorMessage(errorMessage);
		validationDetails.add(detail);
	}

	private String extractEntityNameFromFile(File fileEntity) {
		String[] parts = fileEntity.getFileName().split("_");
		if (parts.length > 1) {
			return parts[1];
		}
		return "";
	}


	public FileDTO getFileStatus(Long id) {
		File file = fileRepository.findById(id).orElse(null);
		if (file != null) {
			return FileMapper.INSTANCE.toDTO(file);
		}
		return null;
	}
}
