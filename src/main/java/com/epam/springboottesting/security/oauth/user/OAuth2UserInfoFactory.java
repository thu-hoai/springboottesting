package com.epam.springboottesting.security.oauth.user;

import java.util.Map;

import com.epam.springboottesting.enums.AuthProvider;
import com.epam.springboottesting.service.exception.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

	private OAuth2UserInfoFactory() {
	}

	public static Oauth2UserInfo getOauth2UserInfor(String registrationId, Map<String, Object> attributes) {

		if (registrationId.equals(AuthProvider.GOOGLE.toString())) {
			return new GoogleOAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException(
					"Login with " + registrationId + " is not supported yet.");
		}
	}
}
