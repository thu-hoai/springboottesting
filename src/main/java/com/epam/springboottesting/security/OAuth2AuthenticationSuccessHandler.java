package com.epam.springboottesting.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		String targetUrl = determineTargetUrl(request, response, authentication);
		// A committed response has already had its status code and headers written
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);

	}

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		JwtUserDetails userPrincipal = (JwtUserDetails) authentication.getPrincipal();

		String token = jwtUtils.generateJwtToken(userPrincipal);

		return UriComponentsBuilder.fromUriString(getDefaultTargetUrl()).queryParam("providerToken", token).build()
				.toUriString();
	}
}
