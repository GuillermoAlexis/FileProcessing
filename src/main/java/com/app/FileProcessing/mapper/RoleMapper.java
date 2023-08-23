package com.app.FileProcessing.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.app.FileProcessing.dto.RoleDTO;
import com.app.FileProcessing.model.Role;

@Mapper(uses = FeatureMapper.class)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "features", source = "features")
    RoleDTO toDTO(Role role);

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDTOList(List<Role> roleList);

    List<Role> toEntityList(List<RoleDTO> roleDTOList);
}
