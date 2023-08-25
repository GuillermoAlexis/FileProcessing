package com.app.FileProcessing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.repository.AppUserRepository;

@Service  // Indica que esta clase es un servicio de Spring
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired  // Inyección de dependencia para el repositorio de usuarios
	private AppUserRepository userRepository;

	@Override
	// Método encargado de cargar un usuario por su email
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    // Busca al usuario en la base de datos por su email
	    AppUser user = userRepository.findByEmail(email);
	    
	    // Si el usuario no se encuentra, lanza una excepción
	    if (user == null) {
	        throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
	    }
	    
	    // Si el usuario se encuentra, devuelve sus detalles
	    return new AppUserDetails(user);
	}
}
