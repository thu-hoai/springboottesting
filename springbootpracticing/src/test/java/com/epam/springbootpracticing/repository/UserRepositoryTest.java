package com.epam.springbootpracticing.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.epam.springbootpracticing.SpringbootpracticingApplication;
import com.epam.springbootpracticing.domain.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringbootpracticingApplication.class)
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldReturnUser_WhenFindByUsername() {

		// Arrange
		User expectedUser = new User();
		expectedUser.setId(1L);
		expectedUser.setUsername("username1");
		String email = "user1@gmail.com";
		expectedUser.setEmail(email);
		userRepository.save(expectedUser);

		// Act
		User actualResult = userRepository.findByUserName("username1");

		// Assert
		Assertions.assertEquals(expectedUser, actualResult);
	}

}
