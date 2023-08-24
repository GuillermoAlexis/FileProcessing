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

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configura AuthenticationManager para que use el servicio de detalles de usuario con BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); //PASO 1 la aplicacion la hecho a correr y aqui inicia
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // PASO 2
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); // PASO 3
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Desactiva CSRF
        httpSecurity.csrf().disable()//PASO 4
                // No necesita autenticación para estas rutas
                .authorizeRequests().antMatchers("/users/login","/users").permitAll()//PASO 5
                // Todas las demás rutas deben ser autenticadas
                .anyRequest().authenticated()//PASO 6
                .and()
                // Asegúrate de que usamos una sesión sin estado; no se creará una sesión
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and().sessionManagement() //PASO 7
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Añade un filtro para validar el token con cada solicitud
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);//PASO 8
    }
}
