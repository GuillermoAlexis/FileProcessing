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

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)//flujo 1
			throws ServletException, java.io.IOException {
		final String authorizationHeader = request.getHeader("Authorization"); //AQUI EJECUTE DESDE POSTMAN EL LOGIN Y E INGRESO ACA PODRIA DECIR QUE ES EL PASO 1 DEL LOGIN

		String email = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { //no ingreso a aqui
			jwt = authorizationHeader.substring(7); // extrae el token sin "Bearer "
			email = jwtProvider.extractEmailFromToken(jwt);
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {//no ingreso a aqui
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			if (jwtProvider.validateToken(jwt)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		filterChain.doFilter(request, response); //PASO 2 DEL LOGIN
	}
}
