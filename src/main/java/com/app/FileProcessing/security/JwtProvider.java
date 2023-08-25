package com.app.FileProcessing.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private Long expiration;

	// Método para extraer el correo electrónico del token
	public String extractEmailFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Método para extraer la fecha de expiración del token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Método genérico para extraer información del token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Método para extraer todas las reclamaciones del token
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
	}

	// Método para verificar si el token ha expirado
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Método para generar un token JWT
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private Key getSecretKey() {
		return Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	// Método para crear un token usando la clave secreta
	private String createToken(Map<String, Object> claims, String subject) {
		Key key = getSecretKey();
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// Método para validar un token
	public Boolean validateToken(String token) {
		final String email = extractEmailFromToken(token);
		return (email != null && !isTokenExpired(token));
	}

	// Método para obtener la autenticación a partir de un token
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(extractEmailFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}
