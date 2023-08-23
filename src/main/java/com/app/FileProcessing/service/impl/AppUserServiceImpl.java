package com.app.FileProcessing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.mapper.AppUserMapper;
import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.repository.AppUserRepository;
import com.app.FileProcessing.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	private AppUserRepository appUserRepository;

	public AppUserDTO createAppUser(AppUserDTO appUserDTO) {
		AppUser appUser = AppUserMapper.INSTANCE.toEntity(appUserDTO);
		return AppUserMapper.INSTANCE.toDTO(appUserRepository.save(appUser));
	}

	public List<AppUserDTO> listAppUsers() {
		List<AppUser> appUsers = appUserRepository.findAll();
		return AppUserMapper.INSTANCE.toDTOList(appUsers);
	}

	public AppUserDTO getAppUserById(Long id) {
		AppUser appUser = appUserRepository.findById(id).orElse(null);
		if (appUser != null) {
			return AppUserMapper.INSTANCE.toDTO(appUser);
		}
		return null;
	}

	public AppUserDTO updateAppUser(Long id, AppUserDTO updatedAppUserDTO) {
		AppUser appUser = appUserRepository.findById(id).orElse(null);
		if (appUser != null) {
			AppUser updatedAppUser = AppUserMapper.INSTANCE.toEntity(updatedAppUserDTO);
			updatedAppUser.setId(id);
			return AppUserMapper.INSTANCE.toDTO(appUserRepository.save(updatedAppUser));
		}
		return null;
	}

	public void deleteAppUser(Long id) {
		appUserRepository.deleteById(id);
	}

	@Override
	public AppUserDTO login(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}
