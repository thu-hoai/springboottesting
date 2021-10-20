package com.epam.springboottesting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.springboottesting.domain.User;
import com.epam.springboottesting.repository.UserRepository;
import com.epam.springboottesting.security.JwtUserDetails;

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
