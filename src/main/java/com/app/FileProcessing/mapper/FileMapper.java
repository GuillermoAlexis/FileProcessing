package com.app.FileProcessing.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.app.FileProcessing.dto.FileDTO;
import com.app.FileProcessing.model.File;

@Mapper
public interface FileMapper {
	FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

	FileDTO toDTO(File file);

	@Mapping(target = "user.roles", ignore = true)
	File toEntity(FileDTO fileDTO);

	List<FileDTO> toDTOList(List<File> fileList);

	List<File> toEntityList(List<FileDTO> fileDTOList);
}
