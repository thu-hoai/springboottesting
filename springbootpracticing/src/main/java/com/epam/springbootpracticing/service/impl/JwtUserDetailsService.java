package com.epam.springbootpracticing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.springbootpracticing.domain.User;
import com.epam.springbootpracticing.repository.UserRepository;
import com.epam.springbootpracticing.security.JwtUserDetails;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public JwtUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Load user from users table by user name
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(username);
		if (user != null) {
			return JwtUserDetails.build(user);
		}
		throw new UsernameNotFoundException("User not found");
	}

}
