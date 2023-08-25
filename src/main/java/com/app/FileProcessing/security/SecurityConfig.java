package com.app.FileProcessing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // Esta clase es una clase de configuración de Spring
@EnableWebSecurity  // Habilita la seguridad web en el proyecto
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtEntryPoint jwtEntryPoint;  // Componente para manejar puntos de acceso no autorizados

	@Autowired
	private UserDetailsService userDetailsService;  // Servicio para obtener detalles de usuario

	@Autowired
	private JwtFilter jwtFilter;  // Filtro para JWT

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Configura AuthenticationManager para que use el servicio de detalles de
		// usuario con BCryptPasswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean  // Define un bean para la codificación de contraseñas
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean  // Define un bean para el AuthenticationManager
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Desactiva CSRF
		httpSecurity.csrf().disable()
				// No necesita autenticación para estas rutas
				.authorizeRequests().antMatchers("/users/login", "/users", "/file/process").permitAll()
				// Todas las demás rutas deben ser autenticadas
				.anyRequest().authenticated()
				.and()
				// Asegúrate de que usamos una sesión sin estado; no se creará una sesión
				.exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Añade un filtro para validar el token con cada solicitud
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
