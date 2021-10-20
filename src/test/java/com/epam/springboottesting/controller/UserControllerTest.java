package com.epam.springboottesting.controller;

import java.util.Set;

import javax.servlet.ServletContext;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.epam.springboottesting.domain.Role;
import com.epam.springboottesting.dto.UserDto;
import com.epam.springboottesting.repository.RoleRepository;
import com.epam.springboottesting.service.impl.UserServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {

		// Mock Spring security
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.addFilter(springSecurityFilterChain).build();
	}

	@Test
	void should_ProvidesController_WhengivenWac() throws Exception {

		final ServletContext servletContext = webApplicationContext.getServletContext();
		Assertions.assertNotNull(servletContext);
		Assertions.assertTrue(servletContext instanceof MockServletContext);
		Assertions.assertNotNull(webApplicationContext.getBean("userController"));
		Assertions.assertNotNull(webApplicationContext.getBean("authenticationController"));
	}

	@Test
	void should_PerformSuccessfulGetUsers_WhenValidToken() throws Exception {
		// Arrange
		Role role = new Role();
		role.setId(1);
		role.setName("Administrator");
		roleRepository.save(role);
		UserDto user = new UserDto();
		user.setId(1L);
		user.setUsername("username1");
		user.setEmail("user1@gmail.com");
		user.setPassword("test#123");
		user.setAuthorities(Set.of(role));
		userService.createUser(user);
		String token = obtainAccessToken("username1", "test#123");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", "Bearer " + token))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void should_Return401_WhenInvalidToken() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	void should_ReturnUser_When_GetUserByIdByAdministrator() throws Exception {
		String token = obtainAccessToken("username1", "test#123");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1").header("Authorization", "Bearer " + token))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is("username1")));
	}

	@Test
	void should_Return403_When_GetUserByIdByNormalUser() throws Exception {
		Role role = new Role();
		role.setId(2);
		role.setName("User");
		roleRepository.save(role);
		UserDto user = new UserDto();
		user.setId(1L);
		user.setUsername("username2");
		user.setEmail("user2@gmail.com");
		user.setPassword("test#123");
		user.setAuthorities(Set.of(role));
		userService.createUser(user);

		String token = obtainAccessToken("username2", "test#123");
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/1").header("Authorization", "Bearer " + token))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	private String obtainAccessToken(String username, String password) throws Exception {

		String jsonBody = "{\r\n" + "    \"username\": \"" + username + "\",\r\n" + "    \"password\": \"" + password
				+ "\"\r\n" + "}";
		ResultActions result = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

		String resultString = result.andReturn().getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("token").toString();
	}

}
