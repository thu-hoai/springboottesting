package com.epam.springboottesting.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * This class is used to read JWT authentication token from the request, verify
 * it and set SecurityContext if the token is valid
 * 
 * @author Hoai_Le
 *
 */
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtTokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	Logger log = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);

	@Value("${app.jwt.header}")
	private String tokenHeader;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("Processing authentication for '{}'", request.getRequestURL());

		// Read JWT authentication token from the request
		final String requestHeader = request.getHeader(tokenHeader);

		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			final String authToken = requestHeader.substring(7);
			final String userName = getUsername(authToken);

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				log.debug("Start authenticating user");

				final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

				if (Boolean.TRUE.equals(jwtTokenUtils.validateToken(authToken, userDetails))) {
					final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					log.info("Authenticated user");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} else {
			log.info("Couldn't find bearer string, ignore the hearder");
		}

		filterChain.doFilter(request, response);

	}

	private String getUsername(String authToken) {
		try {
			return jwtTokenUtils.getUsernameFromToken(authToken);
		} catch (final IllegalArgumentException exp) {
			log.error("An error occured during getting user from token", exp);
		} catch (final ExpiredJwtException exp) {
			logger.error("Expired token", exp);
		}

		return null;
	}

}
