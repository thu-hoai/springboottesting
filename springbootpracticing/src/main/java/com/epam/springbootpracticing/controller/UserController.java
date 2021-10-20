package com.epam.springbootpracticing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.springbootpracticing.dto.UserDto;
import com.epam.springbootpracticing.security.JwtUserDetails;
import com.epam.springbootpracticing.service.IUserService;

@RestController
@RequestMapping("/users")

public class UserController {

	private IUserService userService;

	@Autowired
	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('Administrator')")
	public List<JwtUserDetails> getAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('Administrator')")
	public JwtUserDetails getUserById(@PathVariable Long id) {
		return userService.findUserById(id);
	}

	@PostMapping
	public JwtUserDetails createUser(@RequestBody UserDto user) {
		return userService.createUser(user);
	}

	@PutMapping
	public JwtUserDetails updateUser(@RequestBody UserDto user) {
		return userService.updateUser(user);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('Administrator')")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
	}

}
