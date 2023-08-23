package com.app.FileProcessing.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.app.FileProcessing.dto.FeatureDTO;
import com.app.FileProcessing.model.Feature;

@Mapper
public interface FeatureMapper {
	FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

	FeatureDTO toDTO(Feature feature);

	@Mapping(target = "roles", ignore = true)
	Feature toEntity(FeatureDTO featureDTO);

	List<FeatureDTO> toDTOList(List<Feature> featureList);

	List<Feature> toEntityList(List<FeatureDTO> featureDTOList);
}
