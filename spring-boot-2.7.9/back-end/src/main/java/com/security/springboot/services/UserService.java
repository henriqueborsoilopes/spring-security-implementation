package com.security.springboot.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.springboot.entities.User;
import com.security.springboot.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByEmail(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Email not found"));
		return user.get();
	}
}
