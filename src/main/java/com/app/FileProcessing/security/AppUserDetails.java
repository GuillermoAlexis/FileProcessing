package com.app.FileProcessing.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.FileProcessing.model.AppUser;

public class AppUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AppUser appUser;

	public AppUserDetails(AppUser appUser) {
		this.appUser = appUser; //PASO 8 DEL LOGIN
	}

	// Implementa el método para obtener los roles del usuario
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Transforma los roles en objetos GrantedAuthority
		List<GrantedAuthority> authorities = appUser.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return authorities;
	}

	// Implementa el método para obtener la contraseña del usuario
	@Override
	public String getPassword() {
		return appUser.getPassword();
	}

	// Implementa el método para obtener el nombre de usuario (en este caso, el
	// correo electrónico)
	@Override
	public String getUsername() {
		return appUser.getEmail();
	}

	// Implementa el método para verificar si la cuenta no ha expirado
	@Override
	public boolean isAccountNonExpired() {
		return true; //PASO 11 DEL LOGIN
	}

	// Implementa el método para verificar si la cuenta no está bloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true; //PASO 9 DEL LOGIN
	}

	// Implementa el método para verificar si las credenciales no han expirado
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Implementa el método para verificar si la cuenta está habilitada
	@Override
	public boolean isEnabled() {
		return true; //PASO 10 DEL LOGIN
	}

	public AppUser getAppUser() {
		return appUser;
	}
}
