package com.app.FileProcessing.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.app.FileProcessing.dto.ValidationDetailDTO;
import com.app.FileProcessing.model.ValidationDetail;

@Mapper(uses = FileMapper.class)
public interface ValidationDetailMapper {
	ValidationDetailMapper INSTANCE = Mappers.getMapper(ValidationDetailMapper.class);

	ValidationDetailDTO toDTO(ValidationDetail validationDetail);

	ValidationDetail toEntity(ValidationDetailDTO validationDetailDTO);

	List<ValidationDetailDTO> toDTOList(List<ValidationDetail> validationDetailList);

	List<ValidationDetail> toEntityList(List<ValidationDetailDTO> validationDetailDTOList);
}
