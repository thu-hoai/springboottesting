package com.epam.springbootpracticing.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.springbootpracticing.domain.Role;
import com.epam.springbootpracticing.domain.User;
import com.epam.springbootpracticing.dto.UserDto;
import com.epam.springbootpracticing.enums.UserRole;
import com.epam.springbootpracticing.repository.UserRepository;
import com.epam.springbootpracticing.security.JwtUserDetails;
import com.epam.springbootpracticing.service.IUserService;
import com.epam.springbootpracticing.service.exception.ExistentUserException;
import com.epam.springbootpracticing.service.exception.NonExistentUserException;

@Service
public class UserServiceImpl implements IUserService {

	private UserRepository repository;

	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public JwtUserDetails findUserById(Long id) {
		User entity = repository.findById(id).orElseThrow(() -> new NonExistentUserException("User does not exist"));
		return JwtUserDetails.build(entity);
	}

	@Override
	public JwtUserDetails createUser(UserDto userDto) {

		User existentUser = repository.findByUserName(userDto.getUsername());
		if (existentUser != null) {
			throw new ExistentUserException(String.format("User already exists: %s", userDto.getUsername()));
		}
		User entity = new User();
		entity.setUsername(userDto.getUsername());
		User updatedUser = repository.save(updateUser(entity, userDto));
		return JwtUserDetails.build(updatedUser);
	}

	@Override
	public List<JwtUserDetails> findAllUsers() {
		List<User> users = repository.findAll();
		return users.stream().map(JwtUserDetails::build).collect(Collectors.toList());
	}

	@Override
	public void deleteUserById(Long id) {
		User existentUser = repository.findById(id).orElseThrow(() -> new NonExistentUserException("User not found"));
		repository.deleteById(existentUser.getId());

	}

	@Override
	public JwtUserDetails updateUser(UserDto userDto) {
		User existentUser = repository.findById(userDto.getId())
				.orElseThrow(() -> new NonExistentUserException("User not found"));

		User updatedUser = repository.save(updateUser(existentUser, userDto));

		return JwtUserDetails.build(updatedUser);

	}

	private User updateUser(User entity, UserDto userDto) {

		entity.setEmail(userDto.getEmail());
		entity.setFirstName(userDto.getFirstName());
		entity.setLastName(userDto.getLastName());
		entity.setPassword(passwordEncoder.encode(userDto.getPassword()));

		if (userDto.getAuthorities() != null) {
			entity.setAuthorities(userDto.getAuthorities().stream().map(role -> {
				String roleName = role.getName();
				Role temp = new Role();
				temp.setId(UserRole.valueOf(roleName.toUpperCase()).getRoleId());
				temp.setName(roleName);
				return temp;
			}).collect(Collectors.toSet()));
		}

		return entity;

	}

}
