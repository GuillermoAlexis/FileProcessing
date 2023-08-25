package com.app.FileProcessing.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.FileProcessing.model.AppUser;

public class AppUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L; // ID de serialización para garantizar la compatibilidad entre diferentes versiones de la clase

	private final AppUser appUser; // Objeto de usuario de la aplicación

	// Constructor que asigna el usuario proporcionado al objeto appUser
	public AppUserDetails(AppUser appUser) {
		this.appUser = appUser; //PASO 8 DEL LOGIN
	}

	// Implementación del método para obtener las autoridades (roles) del usuario
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Convierte la lista de roles del usuario en una lista de objetos GrantedAuthority
		List<GrantedAuthority> authorities = appUser.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return authorities;
	}

	// Devuelve la contraseña del usuario
	@Override
	public String getPassword() {
		return appUser.getPassword();
	}

	// Devuelve el nombre de usuario, que en este caso es el email del usuario
	@Override
	public String getUsername() {
		return appUser.getEmail();
	}

	// Indica que la cuenta del usuario no ha expirado
	@Override
	public boolean isAccountNonExpired() {
		return true; //PASO 11 DEL LOGIN
	}

	// Indica que la cuenta del usuario no está bloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true; //PASO 9 DEL LOGIN
	}

	// Indica que las credenciales del usuario no han expirado
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Indica que la cuenta del usuario está activa
	@Override
	public boolean isEnabled() {
		return true; //PASO 10 DEL LOGIN
	}

	// Devuelve el objeto AppUser
	public AppUser getAppUser() {
		return appUser;
	}
}
