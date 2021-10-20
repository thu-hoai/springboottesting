package com.epam.springbootpracticing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.springbootpracticing.security.JwtAuthenticationRequest;
import com.epam.springbootpracticing.security.JwtAuthenticationResponse;
import com.epam.springbootpracticing.security.JwtUtils;
import com.epam.springbootpracticing.service.exception.AuthenticationException;

@RestController
public class AuthenticationController {

	@Value("${app.jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtService;

	@PostMapping("/auth/login")
	public JwtAuthenticationResponse createAuthenticationToken(
			@RequestBody final JwtAuthenticationRequest authenticationRequest) {

		UserDetails userDetails = authenticate(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());

		final String token = jwtService.generateJwtToken(userDetails);

		return new JwtAuthenticationResponse(token, userDetails);
	}

	private UserDetails authenticate(String username, String password) {
		Authentication authentication;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials", e);
		}

		return (UserDetails) authentication.getPrincipal();

	}

}
