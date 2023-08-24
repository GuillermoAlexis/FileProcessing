package com.app.FileProcessing.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.dto.RoleDTO;
import com.app.FileProcessing.mapper.AppUserMapper;
import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.model.Role;
import com.app.FileProcessing.repository.AppUserRepository;
import com.app.FileProcessing.repository.RoleRepository;
import com.app.FileProcessing.security.AppUserDetails;
import com.app.FileProcessing.security.JwtProvider;
import com.app.FileProcessing.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtProvider jwtProvider;

	public AppUserDTO createAppUser(AppUserDTO appUserDTO) {
		// Convierte el DTO a una entidad AppUser
		AppUser appUser = AppUserMapper.INSTANCE.toEntity(appUserDTO);

		// Codifica la contraseña antes de guardarla
		String encodedPassword = passwordEncoder.encode(appUser.getPassword());
		appUser.setPassword(encodedPassword);

		// Asigna roles a la entidad del usuario si es necesario
		// Ejemplo:
		List<Role> roles = roleRepository
				.findAllById(appUserDTO.getRoles().stream().map(RoleDTO::getId).collect(Collectors.toList()));
		appUser.setRoles(roles);

		// Guarda el usuario en la base de datos y convierte la entidad resultante en
		// DTO
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
			throw new RuntimeException("Authentication failed: " + e.getMessage());
		}
	}

}
