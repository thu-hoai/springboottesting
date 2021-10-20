package com.epam.springboottesting.security.oauth;

import java.util.Collections;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.epam.springboottesting.domain.Role;
import com.epam.springboottesting.domain.User;
import com.epam.springboottesting.enums.UserRole;
import com.epam.springboottesting.repository.UserRepository;
import com.epam.springboottesting.security.JwtUserDetails;
import com.epam.springboottesting.security.oauth.user.OAuth2UserInfoFactory;
import com.epam.springboottesting.security.oauth.user.Oauth2UserInfo;
import com.epam.springboottesting.service.exception.OAuth2AuthenticationProcessingException;

@Service
public class CustomOauthUserService extends DefaultOAuth2UserService {

	Logger log = LoggerFactory.getLogger(CustomOauthUserService.class);

	private UserRepository userRepository;

	@Autowired
	public CustomOauthUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {

		// Get user information
		OAuth2User oAuth2User = super.loadUser(userRequest);
		// return an Oauth2User in case authorize success, otherwise raise
		// OAuth2AuthenticationFailureHandler

		return processOauth2User(userRequest, oAuth2User);

	}

	/**
	 * Builds the security principal from the given userReqest. Registers the user
	 * if not already register
	 */
	private OAuth2User processOauth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		// get details user
		Oauth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOauth2UserInfor(
				userRequest.getClientRegistration().getRegistrationId().toUpperCase(), oAuth2User.getAttributes());

		if (oauth2UserInfo.getEmail() == null) {
			throw new OAuth2AuthenticationProcessingException("email not found from Oauth2 provider");
		}

		// Check if the user existed or not.
		// Register a new user if it have not et existed
		User existentUser = userRepository.findByUserName(oauth2UserInfo.getEmail());
		if (existentUser == null) {
			// create a new one in db
			User user = new User();
			Role role = new Role();
			role.setId(UserRole.USER.getRoleId());
			role.setName(StringUtils.capitalize(UserRole.USER.toString().toLowerCase()));
			user.setAuthorities(new HashSet<>(Collections.singletonList(role)));
			user.setUsername(oauth2UserInfo.getEmail());
			user.setFirstName(oauth2UserInfo.getName());
			user.setEmail(oauth2UserInfo.getEmail());
			userRepository.save(user);
			return JwtUserDetails.build(user);
		} else {
			return JwtUserDetails.build(existentUser);
		}

	}
}
