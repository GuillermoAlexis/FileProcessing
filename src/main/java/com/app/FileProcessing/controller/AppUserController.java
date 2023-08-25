package com.app.FileProcessing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.FileProcessing.dto.AppUserDTO;
import com.app.FileProcessing.service.AppUserService;

@RestController
@RequestMapping("/users")
public class AppUserController {
	@Autowired
	private AppUserService appUserService;

	@GetMapping
	public List<AppUserDTO> listAppUsers() {
		return appUserService.listAppUsers();
	}

	@GetMapping("/{userId}")
	public AppUserDTO getAppUserById(@PathVariable Long userId) {
		return appUserService.getAppUserById(userId);
	}

	@PostMapping
	public AppUserDTO createAppUser(@RequestBody AppUserDTO appUserDTO) {
		return appUserService.createAppUser(appUserDTO);
	}

	@PutMapping("/{userId}")
	public AppUserDTO updateAppUser(@PathVariable Long userId, @RequestBody AppUserDTO updatedAppUserDTO) {
		return appUserService.updateAppUser(userId, updatedAppUserDTO);
	}

	@DeleteMapping("/{userId}")
	public void deleteAppUser(@PathVariable Long userId) {
		appUserService.deleteAppUser(userId);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {//PASO 3 DEL LOGIN
	    try {
	        AppUserDTO authenticatedUser = appUserService.login(email, password); //PASO 4 DEL LOGIN
	        return ResponseEntity.ok(authenticatedUser);
	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}

}
