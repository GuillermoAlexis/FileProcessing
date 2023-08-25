package com.app.FileProcessing.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        // Esto es lo que regresará cuando un usuario intenta acceder a un recurso protegido sin autenticarse.
        // En este caso, simplemente retornamos un error 401 y un mensaje simple.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access. Please provide a valid JWT token.");
    }
}
