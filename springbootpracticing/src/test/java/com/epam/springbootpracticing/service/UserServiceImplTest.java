package com.epam.springbootpracticing.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.springbootpracticing.domain.Role;
import com.epam.springbootpracticing.domain.User;
import com.epam.springbootpracticing.dto.UserDto;
import com.epam.springbootpracticing.repository.UserRepository;
import com.epam.springbootpracticing.security.JwtUserDetails;
import com.epam.springbootpracticing.service.exception.ExistentUserException;
import com.epam.springbootpracticing.service.exception.NonExistentUserException;
import com.epam.springbootpracticing.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private UserServiceImpl service;

	User user;

	JwtUserDetails jwtUser;

	UserDto userDto;

	@BeforeEach
	void setUp() {
		// Inject
		service = new UserServiceImpl(userRepository, passwordEncoder);

		// Preapre data
		user = new User();
		user.setId(1L);
		user.setUsername("username");
		user.setPassword("password");
		Role role = new Role();
		role.setId(1);
		role.setName("Administrator");
		user.setAuthorities(Set.of(role));

		userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("username");
		userDto.setPassword("password");
		userDto.setAuthorities(Set.of(role));

		jwtUser = JwtUserDetails.build(user);
	}

	@Test
	void shouldReturnUser_WhenFindById() {
		// Arrange
		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		// Actual
		JwtUserDetails actualResult = service.findUserById(1L);

		Assertions.assertEquals(jwtUser, actualResult);
	}

	@Test
	void should_ThrowNonExistentUser_WhenFindUserById() {
		NonExistentUserException exception = Assertions.assertThrows(NonExistentUserException.class,
				() -> service.findUserById(1L));
		Assertions.assertSame("User does not exist", exception.getMessage());
	}

	@Test
	void should_ReturnUserInfo_WhenCreateUser() {
		// Arrange
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

		JwtUserDetails actualResult = service.createUser(userDto);

		Assertions.assertEquals(jwtUser, actualResult);
	}

	@Test
	void should_RejectCreateUser_WhenUserExisted() {
		Mockito.when(userRepository.findByUserName(Mockito.any())).thenReturn(user);

		ExistentUserException exp = Assertions.assertThrows(ExistentUserException.class,
				() -> service.createUser(userDto));

		Assertions.assertEquals("User already exists: username", exp.getMessage());
	}

	@Test
	void should_LoadAllUsers_WhenFindAllUsers() {
		Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

		List<JwtUserDetails> actualResult = service.findAllUsers();

		Assertions.assertEquals(List.of(jwtUser), actualResult);
	}

	@Test
	void should_UpdateUser_When_ExistentUser() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

		userDto.setEmail("user@gmail.com");
		JwtUserDetails actualResult = service.updateUser(userDto);

		Assertions.assertEquals(userDto.getEmail(), actualResult.getEmail());

	}

	@Test
	void should_RejectUpdateUser_WhenUserNotExistent() {

		NonExistentUserException exception = Assertions.assertThrows(NonExistentUserException.class,
				() -> service.updateUser(userDto));

		Assertions.assertEquals("User not found", exception.getMessage());
	}

	@Test
	void should_deleteUser_WhenUserExisted() {
		Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

		service.deleteUserById(user.getId());

		Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
	}

	@Test
	void should_rejectDeleteUser_When_UserNotFound() {
		NonExistentUserException exception = Assertions.assertThrows(NonExistentUserException.class,
				() -> service.deleteUserById(1L));
		Assertions.assertEquals("User not found", exception.getMessage());
		Mockito.verify(userRepository, Mockito.never()).deleteById(user.getId());
	}

}
