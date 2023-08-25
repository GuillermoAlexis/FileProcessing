package com.app.FileProcessing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.dto.RoleDTO;
import com.app.FileProcessing.mapper.AppUserMapper;
import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.repository.AppUserRepository;
import com.app.FileProcessing.repository.RoleRepository;
import com.app.FileProcessing.security.JwtProvider;
import com.app.FileProcessing.service.impl.AppUserServiceImpl;

class AppUserServiceImplTest {

	@Mock
	private AppUserRepository appUserRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private AppUserServiceImpl appUserService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateAppUser() {
		// Prepare
		AppUserDTO appUserDTO = new AppUserDTO();
		appUserDTO.setUserName("user123");
		appUserDTO.setEmail("user@example.com");
		appUserDTO.setPassword("password");
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setId(1L);
		appUserDTO.setRoles(List.of(roleDTO));

		AppUser appUser = AppUserMapper.INSTANCE.toEntity(appUserDTO);
		when(roleRepository.findAllById(anyList())).thenReturn(new ArrayList<>());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

		// Execute
		AppUserDTO createdAppUserDTO = appUserService.createAppUser(appUserDTO);

		// Verify
		assertNotNull(createdAppUserDTO);
		assertEquals(appUserDTO.getUserName(), createdAppUserDTO.getUserName());
		assertEquals(appUserDTO.getEmail(), createdAppUserDTO.getEmail());
		assertEquals(appUserDTO.getRoles().size(), createdAppUserDTO.getRoles().size());
	}

}
