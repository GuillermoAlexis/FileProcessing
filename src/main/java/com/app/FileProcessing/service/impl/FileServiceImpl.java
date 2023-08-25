package com.app.FileProcessing.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.FileProcessing.dto.FileDTO;
import com.app.FileProcessing.mapper.FileMapper;
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

		// Procesar de manera asíncrona el contenido del archivo
		asyncProcessFileContent(file, fileEntity);

		fileEntity = fileRepository.save(fileEntity);

		// Retornar la respuesta
		return FileMapper.INSTANCE.toDTO(fileEntity);
	}

	@Async
	private void asyncProcessFileContent(MultipartFile file, File fileEntity) {
		
	}

	@Override
	public FileDTO getFileStatus(Long fileId) {
		// TODO Auto-generated method stub
		return null;
	}
}
