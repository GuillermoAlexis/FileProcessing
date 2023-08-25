package com.app.FileProcessing.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component  // Indica que esta clase es un componente de Spring
public class JwtFilter extends OncePerRequestFilter {

	@Autowired  // Inyección de dependencia para el proveedor de JWT
	private JwtProvider jwtProvider;

	@Autowired  // Inyección de dependencia para el servicio de detalles de usuario
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) // Flujo 1
			throws ServletException, java.io.IOException {
		// Obtiene el encabezado de autorización de la solicitud
		final String authorizationHeader = request.getHeader("Authorization"); // Paso 1 del login: se obtiene el encabezado desde Postman

		String email = null;
		String jwt = null;

		// Verifica si el encabezado de autorización es válido
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7); // Extrae el token sin el prefijo "Bearer "
			email = jwtProvider.extractEmailFromToken(jwt);  // Extrae el correo electrónico del token
		}

		// Verifica si el email se extrajo correctamente y si no hay una autenticación actual
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Carga los detalles del usuario a partir del email
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			// Verifica si el token es válido
			if (jwtProvider.validateToken(jwt)) {
				// Crea una autenticación y la establece en el contexto de seguridad
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		// Continúa con el siguiente filtro en la cadena
		filterChain.doFilter(request, response); // Paso 2 del login: se pasa al siguiente filtro
	}
}
