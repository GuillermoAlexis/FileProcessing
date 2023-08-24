package com.app.FileProcessing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.mapper.AppUserMapper;
import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.repository.AppUserRepository;
import com.app.FileProcessing.security.AppUserDetails;
import com.app.FileProcessing.security.JwtProvider;
import com.app.FileProcessing.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtProvider jwtProvider;

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

	public AppUserDTO login(String email, String password) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			// El objeto de autenticación incluye la información del usuario autenticado
			AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
			AppUser appUser = userDetails.getAppUser();

			String jwtToken = jwtProvider.generateToken(userDetails);

			AppUserDTO appUserDTO = AppUserMapper.INSTANCE.toDTO(appUser);
			appUserDTO.setToken(jwtToken);

			return appUserDTO;
		} catch (AuthenticationException e) {
			// Manejar el fallo de autenticación
			throw new RuntimeException("Authentication failed");
		}
	}
}
