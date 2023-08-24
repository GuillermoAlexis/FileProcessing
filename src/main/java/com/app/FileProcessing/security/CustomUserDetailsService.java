package com.app.FileProcessing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.FileProcessing.model.AppUser;
import com.app.FileProcessing.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    AppUser user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
	    }
	    return new AppUserDetails(user);
	}


}
