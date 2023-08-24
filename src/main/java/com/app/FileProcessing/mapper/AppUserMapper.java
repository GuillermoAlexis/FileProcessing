package com.app.FileProcessing.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.model.AppUser;

@Mapper(uses = RoleMapper.class)
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "token", ignore = true)
    AppUserDTO toDTO(AppUser appUser);

    AppUser toEntity(AppUserDTO appUserDTO);

    List<AppUserDTO> toDTOList(List<AppUser> appUserList);

    List<AppUser> toEntityList(List<AppUserDTO> appUserDTOList);
}
